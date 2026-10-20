package com.example.project.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long targetUserId;
    private String targetUsername;
    private String message;
    private String type;
    private String content;
    private String messageId;
}
