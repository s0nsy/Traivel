package com.example.project.controller;

import com.example.project.entity.dto.AddPlaceToRouteRequest;
import com.example.project.entity.dto.NaverSearchRequest;
import com.example.project.service.NaverSearchService;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/naver")
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
   @PostMapping("/pin")
   public ResponseEntity<String> addPin(@RequestBody AddPlaceToRouteRequest request){
     routeService.addPin(request);
     return ResponseEntity.ok("장소를 추가했습니다.");
   }



}
