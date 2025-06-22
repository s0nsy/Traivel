package com.example.project.entity.dto;

import lombok.Data;
@Data
public class AddPlaceToRouteRequest {
   private Long routeId;
   private NaverSearchRequest naverSearchRequest;
   private int order;
   private int day;
   private String dType;
}
