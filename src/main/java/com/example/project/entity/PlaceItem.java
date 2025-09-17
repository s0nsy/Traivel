package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name="place_item")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy=InheritanceType.JOINED)
public class PlaceItem extends RouteItem{

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="place_id")
   private Place place;

}
