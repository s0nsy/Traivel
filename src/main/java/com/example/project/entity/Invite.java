package com.example.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invite {
   private Long id;

   private String token;

   private Long route_id;

   private LocalDateTime expired_at;

}
