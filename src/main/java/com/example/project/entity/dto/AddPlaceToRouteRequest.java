package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class AddPlaceToRouteRequest {
   private Long routeId;
   private NaverSearchRequest naverSearchRequest;
   private int order;
   private int day;
   private String dtype;
   @Schema(example = "PLACE")
   public String getDtype(){
      return dtype;
   }
   // Springdoc 1.x에서 사용하는 기본 getter 기반 접근

}
