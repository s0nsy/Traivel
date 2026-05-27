package com.example.project.controller;

import com.example.project.entity.dto.AddPlaceToRouteRequest;
import com.example.project.entity.dto.NaverPlaceDto;
import com.example.project.service.NaverSearchService;
import com.example.project.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/naver")
@RequiredArgsConstructor
public class NaverController {
   private final NaverSearchService naverSearchService;
   private final RouteService routeService;

   @GetMapping("/search")
   public ResponseEntity<List<NaverPlaceDto>> search(@RequestParam String keyword) {
      log.info("장소 검색 요청: keyword='{}'", keyword);
      List<NaverPlaceDto> list = naverSearchService.searchPlace(keyword);
      if (list.isEmpty()) {
         log.info("장소 검색 결과 없음: keyword='{}'", keyword);
         return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(list);
   }

   @PostMapping("/pin")
   public ResponseEntity<String> addPin(@Valid @RequestBody AddPlaceToRouteRequest request) {
      log.info("장소 핀 추가 요청: 장소='{}', 루트ID={}", request.getPlace().getTitle(), request.getRouteId());
      routeService.addPin(request);
      return ResponseEntity.ok("장소를 추가했습니다.");
   }
}
