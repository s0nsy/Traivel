package com.example.project.controller;

import com.example.project.entity.dto.RouteRequest;
import com.example.project.entity.dto.TravelRequest;
import com.example.project.entity.dto.TravelResponse;
import com.example.project.service.ChatGPTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatGPTController {
   private final ChatGPTService chatGPTService;

   // 여행지 추천
   @PostMapping("/recommend")
   public ResponseEntity<List<TravelResponse>> getRecommendDestinations(@RequestBody TravelRequest request) throws JsonProcessingException {
      List<TravelResponse> recommendations = chatGPTService.getTravelRecommendations(request);
      return ResponseEntity.ok(recommendations);
   }

   // 추천 루트 출력
   @PostMapping("/route")
   public ResponseEntity<String> getRecommendRoute(@RequestBody RouteRequest routeRequest) throws JsonProcessingException {
      String route = chatGPTService.getRouteRecommendation(routeRequest);
      return ResponseEntity.ok(route);
   }
}
