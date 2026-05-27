package com.example.project.service;

import com.example.project.exception.BusinessException;
import com.example.project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

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
            if (Boolean.TRUE.equals(redis.opsForValue().setIfAbsent(key, token, Duration.ofMillis(TTL_MS)))) {
                return token;
            }
            try {
                Thread.sleep(SPIN_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException(ErrorCode.ROUTE_LOCK_TIMEOUT);
            }
        }
        throw new BusinessException(ErrorCode.ROUTE_LOCK_TIMEOUT);
    }

    public void release(Long routeId, String token) {
        redis.execute(RELEASE_SCRIPT, Collections.singletonList(LOCK_PREFIX + routeId), token);
    }
}
