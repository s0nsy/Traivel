package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class AddPlaceToRouteRequest {
   private Long routeId;
   private NaverSearchRequest naverSearchRequest;
   private int order;
   private int day;
   @Schema(example = "PLACE")
   private String dType;
}
