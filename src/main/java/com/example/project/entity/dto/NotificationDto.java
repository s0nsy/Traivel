package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
   private String message;
   private String Type;
   @Schema(example="false")
   private boolean isRead;
}
