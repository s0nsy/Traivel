package com.example.project.entity.dto;

import com.example.project.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class NotificationDto {
   private String message;
   private String Type;
   @Schema(example="false")
   private boolean isRead;
   private String content;

   public NotificationDto(Notification noti){
      this.message= noti.getMessage();
      this.Type=noti.getType();
      this.isRead= noti.isRead();
      this.content=noti.getContent();
   }
}
