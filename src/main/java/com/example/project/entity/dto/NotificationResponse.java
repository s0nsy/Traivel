package com.example.project.entity.dto;

import com.example.project.entity.Notification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
   private String message;
   private String type;
   private boolean isRead;
   private String content;
   private LocalDateTime createdAt;

   public NotificationResponse(Notification noti) {
      this.message = noti.getMessage();
      this.type = noti.getType();
      this.isRead = noti.isRead();
      this.content = noti.getContent();
      this.createdAt = noti.getCreatedAt();
   }
}
