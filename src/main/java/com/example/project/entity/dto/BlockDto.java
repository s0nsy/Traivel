package com.example.project.entity.dto;

import com.example.project.entity.Route;
import com.example.project.entity.RouteItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockDto {
   private Long id;
   private int order;
   private int day;
   private Route route;

   public BlockDto(RouteItem routeItem){
      this.id = routeItem.getId();
      this.order= routeItem.getOrder();
      this.day=routeItem.getDay();
      this.route=routeItem.getRoute();
   }
}
