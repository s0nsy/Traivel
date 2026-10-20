package com.example.project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinationConsumer {

    public static final String REDIS_KEY = "popular:destinations";
    private static final String PROCESSED_KEY_PREFIX = "dest:processed:";
    // Kafka @RetryableTopic 재시도 간격(최대 약 3초)보다 충분히 긴 TTL로 중복 처리 방지
    private static final Duration DEDUP_TTL = Duration.ofDays(1);

    private final StringRedisTemplate redisTemplate;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "destination-event", groupId = "destination-group")
    public void consume(DestinationEvent event) {
        log.info("여행지 이벤트 소비: messageId={}, destination={}", event.getMessageId(), event.getDestination());

        // messageId 기반 멱등성 처리: 이미 처리된 메시지는 중복 집계하지 않음
        String processedKey = PROCESSED_KEY_PREFIX + event.getMessageId();
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(processedKey, "1", DEDUP_TTL);
        if (!Boolean.TRUE.equals(isNew)) {
            log.warn("중복 메시지 무시: messageId={}, destination={}", event.getMessageId(), event.getDestination());
            return;
        }

        redisTemplate.opsForZSet().incrementScore(REDIS_KEY, event.getDestination(), 1);
        log.info("인기 여행지 집계 완료: {}", event.getDestination());
    }
}
