package com.example.project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinationProducer {

    private static final String TOPIC = "destination-event";
    private final KafkaTemplate<String, DestinationEvent> kafkaTemplate;

    public void send(String destination) {
        String messageId = UUID.randomUUID().toString();
        kafkaTemplate.send(TOPIC, destination, new DestinationEvent(messageId, destination));
        log.info("여행지 이벤트 발행: topic={}, messageId={}, destination={}", TOPIC, messageId, destination);
    }
}
