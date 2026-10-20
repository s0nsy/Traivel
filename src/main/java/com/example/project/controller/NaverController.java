package com.example.project.controller;

import com.example.project.entity.dto.AddPlaceToRouteRequest;
import com.example.project.entity.dto.NaverPlaceDto;
import com.example.project.service.NaverSearchService;
import com.example.project.service.RouteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/naver")
@RequiredArgsConstructor
public class NaverController {
   private final NaverSearchService naverSearchService;
   private final RouteService routeService;

   @Autowired
   @Qualifier("naverMapClient")
   private WebClient naverMapClient;

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

   @GetMapping(value = "/static-map", produces = MediaType.IMAGE_PNG_VALUE)
   public ResponseEntity<byte[]> staticMap(HttpServletRequest request) {
      String queryString = request.getQueryString();
      URI uri = URI.create("https://maps.apigw.ntruss.com/map-static/v2/raster"
            + (queryString != null ? "?" + queryString : ""));
      log.info("정적 지도 프록시 요청: {}", uri);
      byte[] imageBytes = naverMapClient.get()
            .uri(uri)
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                  clientResponse.bodyToMono(String.class)
                        .defaultIfEmpty("[empty body]")
                        .doOnNext(body -> log.error("NCP Static Map 오류: status={}, body={}", clientResponse.statusCode(), body))
                        .then(clientResponse.createException()))
            .bodyToMono(byte[].class)
            .block();
      return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(imageBytes);
   }

   @PostMapping("/pin")
   public ResponseEntity<String> addPin(@Valid @RequestBody AddPlaceToRouteRequest request, Principal principal) {
      log.info("장소 핀 추가 요청: 장소='{}', 루트ID={}", request.getPlace().getTitle(), request.getRouteId());
      routeService.addPin(request, principal.getName());
      return ResponseEntity.ok("장소를 추가했습니다.");
   }
}
