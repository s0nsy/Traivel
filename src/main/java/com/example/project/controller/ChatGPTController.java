package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.dto.RecommendRouteRequest;
import com.example.project.entity.dto.RouteRecommendationResponse;
import com.example.project.entity.dto.TravelInfoRequest;
import com.example.project.entity.dto.TravelInfoResponse;
import com.example.project.entity.dto.TravelRequest;
import com.example.project.entity.dto.TravelResponse;
import com.example.project.kafka.DestinationProducer;
import com.example.project.repository.RouteRepository;
import com.example.project.service.ChatGPTService;
import com.example.project.service.NaverSearchService;
import com.example.project.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatGPTController {
   private final ChatGPTService chatGPTService;
   private final NaverSearchService naverSearchService;
   private final UserService userService;
   private final RouteRepository routeRepository;
   private final RouteController routeController;
   private final DestinationProducer destinationProducer;

   @PostMapping("/info")
   public ResponseEntity<TravelInfoResponse> getTravelInfo(
         @RequestBody TravelInfoRequest request) {
      log.info("여행 부가정보 요청: 도시={}", request.getCity());
      List<com.example.project.entity.dto.NaverPlaceDto> hotels;
      try {
         String query = request.getCity() + " " + request.getAccommodation();
         hotels = naverSearchService.searchPlace(query).stream()
               .filter(h -> h.getCategory() != null && h.getCategory().startsWith("숙박"))
               .limit(3)
               .toList();
         // 숙박 카테고리 결과가 부족하면 필터 없이 재시도
         if (hotels.isEmpty()) {
            hotels = naverSearchService.searchPlace(request.getCity() + " 숙박").stream()
                  .filter(h -> h.getCategory() != null && h.getCategory().startsWith("숙박"))
                  .limit(3)
                  .toList();
         }
         hotels.forEach(h -> {
            String name = h.getTitle().replaceAll("<[^>]+>", "");
            h.setImageUrl(naverSearchService.searchImageThumbnail(name));
         });
      } catch (Exception e) {
         log.warn("숙박 검색 실패: {}", e.getMessage());
         hotels = List.of();
      }
      List<com.example.project.entity.dto.HighlightDto> highlights;
      try {
         highlights = chatGPTService.getHighlights(request);
      } catch (Exception e) {
         log.warn("현지 트렌드 생성 실패: {}", e.getMessage());
         highlights = List.of();
      }
      return ResponseEntity.ok(new TravelInfoResponse(hotels, highlights));
   }

   @PostMapping("/recommend")
   public ResponseEntity<List<TravelResponse>> getRecommendDestinations(
         @Valid @RequestBody TravelRequest request) throws JsonProcessingException {
      log.info("여행지 추천 요청: 일정={}", request.getSchedule());
      List<TravelResponse> recommendations = chatGPTService.getTravelRecommendations(request);
      return ResponseEntity.ok(recommendations);
   }

   @PostMapping("/route")
   @Operation(description = "features 배열에 3가지를 특징을 적어주세요.")
   public ResponseEntity<RouteRecommendationResponse> getRecommendRoute(
         @Valid @RequestBody RecommendRouteRequest routeRequest) throws JsonProcessingException {
      log.info("루트 추천 요청: 목적지={}", routeRequest.getDestination());
      RouteRecommendationResponse route = chatGPTService.getRouteRecommendation(routeRequest);
      return ResponseEntity.ok(route);
   }

   @PostMapping("/route/adjust")
   public ResponseEntity<String> adjustRoute(
         Principal principal,
         @RequestParam String routeId,
         @Valid @RequestBody RouteRecommendationResponse routeData) throws AccessDeniedException {
      log.info("AI 루트 적용 요청: routeId={}, 사용자={}", routeId, principal.getName());
      User user = userService.findByUsername(principal.getName());
      if (user == null) throw new AccessDeniedException("루트를 추가할 권한이 없습니다.");

      Long parsedRouteId;
      if (routeId.equals("null") || routeId.equals("undefined") || routeId.isEmpty()) {
         parsedRouteId = routeController.addRoute(principal, routeData.getDestination());
         destinationProducer.send(routeData.getDestination());
      } else {
         try {
            parsedRouteId = Long.parseLong(routeId);
            if (!routeRepository.findUsersByRouteId(parsedRouteId).contains(user))
               throw new AccessDeniedException("루트를 추가할 권한이 없습니다.");
         } catch (NumberFormatException e) {
            log.warn("잘못된 routeId 형식: {}", routeId);
            throw new IllegalArgumentException("잘못된 routeId " + routeId);
         }
      }
      chatGPTService.adjustRoute(parsedRouteId, routeData);
      log.info("AI 루트 적용 완료: 루트ID={}", parsedRouteId);
      return ResponseEntity.ok("루트가 적용되었습니다. 루트: " + parsedRouteId);
   }
}
