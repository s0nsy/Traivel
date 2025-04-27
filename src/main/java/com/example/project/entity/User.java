package com.example.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (name = "users")
public class User {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   private String username;

   private String password;

   private String email;

   private String role;
}
