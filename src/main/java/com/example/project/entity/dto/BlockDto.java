package com.example.project.entity.dto;

import com.example.project.entity.MemoItem;
import com.example.project.entity.PlaceItem;
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
   private Long routeId;
   private int order;
   private int day;
   private String dtype;
   private String title; // place.title or memo.content
   private String address; // memo는 null
   private Long memoId;
   private Long placeId;

   public BlockDto(RouteItem routeItem){
      this.id = routeItem.getId();
      this.order= routeItem.getOrder();
      this.day = routeItem.getDay();
      this.dtype= routeItem.getDtype();
      if(routeItem instanceof PlaceItem placeItem){
         this.title= placeItem.getPlace().getTitle();
         this.address = placeItem.getPlace().getAddress();
         this.placeId= placeItem.getPlace().getId();
      }else if(routeItem instanceof MemoItem memoItem){
         this.title = memoItem.getMemo().getContent();
         this.address= null;
         this.memoId= memoItem.getMemo().getId();
      }
   }
}
