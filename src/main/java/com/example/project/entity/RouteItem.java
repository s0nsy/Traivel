package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="route_item")
public abstract class RouteItem {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(name="`order`")
   private int order;
   private int day;

   @ManyToOne
   @JoinColumn(name="route_id")
   private Route route;
}
