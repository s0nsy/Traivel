package com.example.project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private static final String TOPIC = "notification";
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void send(NotificationEvent event) {
        event.setMessageId(UUID.randomUUID().toString());
        kafkaTemplate.send(TOPIC, event.getTargetUsername(), event);
        log.info("알림 이벤트 발행: topic={}, messageId={}, 대상={}, type={}", TOPIC, event.getMessageId(), event.getTargetUsername(), event.getType());
    }
}
