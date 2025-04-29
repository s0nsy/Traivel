package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class RouteRequest {
   private TravelRequest travelRequest;
   @Schema(example="전라남도 순천")
   private String destination;
   private List<String> features;
}
