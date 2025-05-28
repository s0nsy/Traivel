package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="place_item")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorValue("PLACE")
public class PlaceItem extends RouteItem{

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="place_id")
   private Place place;

}
