package com.example.project.controller;

import com.example.project.entity.dto.TravelRequest;
import com.example.project.service.ChatGPTService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatGPTController {
   private final ChatGPTService chatGPTService;

   @PostMapping("/recommend")
   public ResponseEntity<String> getRecommendDestinations(@RequestBody TravelRequest request){
      String recommendations = chatGPTService.getTravelRecommendations(request);
      return ResponseEntity.ok(recommendations);
   }
}
