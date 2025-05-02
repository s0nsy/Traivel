package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="place_item")
public class PlaceItem extends RouteItem{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="place_id")
   private Place place;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="route_id")
   private Route route;

}
