package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class AddPlaceToRouteRequest {
   private Long routeId;
   private NaverSearchRequest naverSearchRequest;
   private int order;
   private int day;
}
