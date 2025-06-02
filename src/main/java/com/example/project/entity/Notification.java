package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
   @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
   private Long id;

   private Long userId;
   private String message;
   private String Type;
   private boolean isRead;
   private LocalDateTime createdAt;

}
