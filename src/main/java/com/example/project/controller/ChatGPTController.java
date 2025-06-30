package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.dto.RecommendRouteRequest;
import com.example.project.entity.dto.TravelRequest;
import com.example.project.entity.dto.TravelResponse;
import com.example.project.mapper.PlaceMapper;
import com.example.project.mapper.UserMapper;
import com.example.project.service.ChatGPTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatGPTController {
   private final ChatGPTService chatGPTService;
   private final UserMapper userMapper;
   private final PlaceMapper placeMapper;

   // 여행지 추천
   @PostMapping("/recommend")
   public ResponseEntity<List<TravelResponse>> getRecommendDestinations(@RequestBody TravelRequest request) throws JsonProcessingException {
      List<TravelResponse> recommendations = chatGPTService.getTravelRecommendations(request);
      return ResponseEntity.ok(recommendations);
   }

   // 추천 루트 출력
   @PostMapping("/route")
   @Operation(description = "features 배열에 3가지를 특징을 적어주세요.")
   public ResponseEntity<String> getRecommendRoute(@RequestBody RecommendRouteRequest routeRequest) throws JsonProcessingException {
      String route = chatGPTService.getRouteRecommendation(routeRequest);
      return ResponseEntity.ok(route);
   }

   // 출력된 추천 루트 적용
   @PostMapping("/route/adjust")
   public ResponseEntity<String> adjustRoute(Long routeId, @RequestBody String text, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      if(user==null||!placeMapper.findUsersByRouteId(routeId).contains(user))
         throw new AccessDeniedException("루트를 추가할 권한이 없습니다.");
      chatGPTService.adjustRoute(routeId,text);
      return ResponseEntity.ok("루트가 적용되었습니다.");
   }
}
