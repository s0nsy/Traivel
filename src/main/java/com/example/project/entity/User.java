package com.example.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

   @ManyToMany(mappedBy="users", cascade = CascadeType.ALL)
   private List<Route> route= new ArrayList<>();
}
