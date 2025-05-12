package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String title;

   private LocalDateTime created_at;

   @OneToMany(mappedBy="route", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<RouteItem> items= new ArrayList<>();

   @ManyToMany(fetch= FetchType.LAZY)
   @JoinTable(
         name="route_user",
         joinColumns = @JoinColumn(name="route_id"),
         inverseJoinColumns = @JoinColumn(name="user_id")
   )
   private List<User> users= new ArrayList<>();

   @ManyToOne(fetch=FetchType.LAZY)
   @JoinColumn(name="owner_id")
   private User owner;
}
