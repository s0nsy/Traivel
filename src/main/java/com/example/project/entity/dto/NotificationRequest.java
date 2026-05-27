package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NotificationRequest {
   private String message;
   private String type;
   @Schema(example = "false")
   private boolean isRead;
}
