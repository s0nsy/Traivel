package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype")
@Table(name="route_item")
public abstract class RouteItem {
   private Long id;
   @Column(name="`order`")
   private int order;
   private int day;

   @ManyToOne
   @JoinColumn(name="route_id")
   private Route route;

   @Column(insertable = false, updatable = false)
   private String dtype;
}
