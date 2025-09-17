package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
   private Long id;

   private Long userId;
   private String message;
   private String Type;
   private boolean isRead;
   private LocalDateTime createdAt;
   private String content;

}
