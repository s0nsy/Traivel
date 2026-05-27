package com.example.project.entity.dto;

import com.example.project.entity.Notification;
import lombok.Data;

@Data
public class NotificationResponse {
   private String message;
   private String type;
   private boolean isRead;
   private String content;

   public NotificationResponse(Notification noti) {
      this.message = noti.getMessage();
      this.type = noti.getType();
      this.isRead = noti.isRead();
      this.content = noti.getContent();
   }
}
