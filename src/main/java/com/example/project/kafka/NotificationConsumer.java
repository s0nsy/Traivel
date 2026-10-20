package com.example.project.kafka;

import com.example.project.entity.Notification;
import com.example.project.entity.dto.NotificationResponse;
import com.example.project.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private static final String PROCESSED_KEY_PREFIX = "notif:processed:";
    private static final Duration DEDUP_TTL = Duration.ofDays(1);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate redisTemplate;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "notification", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        log.info("알림 이벤트 소비: messageId={}, 대상={}, type={}", event.getMessageId(), event.getTargetUsername(), event.getType());

        // messageId 기반 멱등성 처리: 재시도로 인한 중복 DB 저장 및 WebSocket push 방지
        String processedKey = PROCESSED_KEY_PREFIX + event.getMessageId();
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(processedKey, "1", DEDUP_TTL);
        if (!Boolean.TRUE.equals(isNew)) {
            log.warn("중복 알림 메시지 무시: messageId={}, 대상={}", event.getMessageId(), event.getTargetUsername());
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(event.getTargetUserId());
        notification.setMessage(event.getMessage());
        notification.setType(event.getType());
        notification.setContent(event.getContent());
        notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                event.getTargetUsername(),
                "/queue/notify",
                new NotificationResponse(notification)
        );
        log.info("알림 WebSocket push 완료: 대상={}", event.getTargetUsername());
    }
}
