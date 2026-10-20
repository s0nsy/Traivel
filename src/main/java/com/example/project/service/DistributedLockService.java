package com.example.project.service;

import com.example.project.exception.BusinessException;
import com.example.project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private static final String LOCK_PREFIX = "route:lock:";
    private static final long TTL_MS = 5_000L;
    private static final long SPIN_INTERVAL_MS = 50L;

    private static final DefaultRedisScript<Long> RELEASE_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end",
            Long.class
    );

    private final StringRedisTemplate redis;

    public String acquire(Long routeId) {
        String key = LOCK_PREFIX + routeId;
        String token = UUID.randomUUID().toString();
        long deadline = System.currentTimeMillis() + TTL_MS;

        while (System.currentTimeMillis() < deadline) {
            try {
                if (Boolean.TRUE.equals(redis.opsForValue().setIfAbsent(key, token, Duration.ofMillis(TTL_MS)))) {
                    return token;
                }
            } catch (Exception e) {
                log.error("Redis 연결 실패로 분산 락 획득 불가: routeId={}", routeId, e);
                throw new BusinessException(ErrorCode.ROUTE_LOCK_TIMEOUT);
            }
            try {
                Thread.sleep(SPIN_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException(ErrorCode.ROUTE_LOCK_TIMEOUT);
            }
        }
        log.warn("분산 락 획득 타임아웃: routeId={}", routeId);
        throw new BusinessException(ErrorCode.ROUTE_LOCK_TIMEOUT);
    }

    public void release(Long routeId, String token) {
        try {
            redis.execute(RELEASE_SCRIPT, Collections.singletonList(LOCK_PREFIX + routeId), token);
        } catch (Exception e) {
            // 락 해제 실패 시 TTL(5초) 만료 후 자동 해제
            log.error("분산 락 해제 실패 (TTL 만료 후 자동 해제 예정): routeId={}", routeId, e);
        }
    }
}
