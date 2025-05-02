package com.example.project.controller;

import com.example.project.config.WebClientConfig;
import com.example.project.entity.dto.AddPlaceToRouteRequest;
import com.example.project.entity.dto.NaverSearchRequest;
import com.example.project.service.NaverSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class NaverController {
   private final NaverSearchService naverSearchService;
   private final WebClientConfig webClientConfig;

   @GetMapping("/search")
   public ResponseEntity<List<NaverSearchRequest>> search(@RequestParam String keyword){
      List<NaverSearchRequest> list = naverSearchService.searchPlace(keyword);
      if(list.isEmpty())
         return ResponseEntity.noContent().build();
      return ResponseEntity.ok(list);
   }

   @PostMapping("/addPin")
   public ResponseEntity<String> addPin(@RequestBody AddPlaceToRouteRequest request){
     naverSearchService.addPin(request);
     return ResponseEntity.ok("장소를 추가했습니다.");
   }

   @PostMapping("/addPin/map")
   public ResponseEntity<String> reverseGeocode(@RequestBody Map<String, Double> coords){
      String address = naverSearchService.reverseGeocode(coords);
      return ResponseEntity.ok(address);
   }
}
