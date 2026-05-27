package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "place_item")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("PLACE")
public class PlaceItem extends RouteItem {

   @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinColumn(name = "place_id")
   private Place place;
}
