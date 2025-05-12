package com.example.project.controller;

import com.example.project.config.WebClientConfig;
import com.example.project.entity.dto.AddPlaceToRouteRequest;
import com.example.project.entity.dto.NaverSearchRequest;
import com.example.project.service.NaverSearchService;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NaverController {
   private final NaverSearchService naverSearchService;
   private final RouteService routeService;

   // 네이버 검색 API 검색
   @GetMapping("/search")
   public ResponseEntity<List<NaverSearchRequest>> search(@RequestParam String keyword){
      List<NaverSearchRequest> list = naverSearchService.searchPlace(keyword);
      if(list.isEmpty())
         return ResponseEntity.noContent().build();
      return ResponseEntity.ok(list);
   }

   // 장소 추가
   @PostMapping("/addPin")
   public ResponseEntity<String> addPin(@RequestBody AddPlaceToRouteRequest request){
     routeService.addPin(request);
     return ResponseEntity.ok("장소를 추가했습니다.");
   }

   // 네이버 지도에서 장소 추가
   @PostMapping("/addPin/map")
   public ResponseEntity<String> reverseGeocode(@RequestBody Map<String, Double> coords){
      String address = naverSearchService.reverseGeocode(coords);
      return ResponseEntity.ok(address);
   }
}
