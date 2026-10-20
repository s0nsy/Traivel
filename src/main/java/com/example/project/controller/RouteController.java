package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.dto.*;
import com.example.project.service.RouteService;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteController {
   private final RouteService routeService;
   private final UserService userService;

   @GetMapping
   public ResponseEntity<List<RouteListDto>> getMyRoutes(Principal principal) {
      log.debug("내 루트 목록 조회: 사용자={}", principal.getName());
      return ResponseEntity.ok(routeService.getMyRoutes(principal.getName()));
   }

   @PostMapping
   public Long addRoute(Principal principal, String title) {
      log.info("루트 추가 요청: 제목='{}', 사용자={}", title, principal.getName());
      Long routeId = routeService.addRoute(principal.getName(), title);
      return routeId;
   }

   @GetMapping("/{routeId}")
   public ResponseEntity<Collection<BlockDto>> getRoute(@PathVariable Long routeId, Principal principal) {
      log.debug("루트 조회: ID={}", routeId);
      return ResponseEntity.ok(routeService.getAllBlocks(routeId, principal.getName()));
   }

   @PostMapping("/place")
   public ResponseEntity<String> addPlace(@RequestBody Map<String, Object> body, Principal principal) {
      Long routeId = ((Number) body.get("routeId")).longValue();
      String name = (String) body.get("name");
      String hours = (String) body.getOrDefault("hours", "");
      String tip = (String) body.getOrDefault("tip", "");
      int day = ((Number) body.get("day")).intValue();
      log.info("장소 추가 요청(이름): 루트ID={}, 장소='{}', day={}", routeId, name, day);
      routeService.assertMember(routeId, principal.getName());
      routeService.addPlaceByName(routeId, name, hours, tip, day);
      return ResponseEntity.ok("장소가 추가되었습니다.");
   }

   @PostMapping("/memo")
   public ResponseEntity<String> addMemo(@Valid @RequestBody MemoRequest memoRequest, Principal principal) {
      log.info("메모 추가 요청: 루트ID={}, day={}", memoRequest.getRouteId(), memoRequest.getDay());
      routeService.assertMember(memoRequest.getRouteId(), principal.getName());
      routeService.addMemo(memoRequest);
      return ResponseEntity.ok("메모를 추가했습니다.");
   }

   @PutMapping("/memo/{memoId}")
   public ResponseEntity<String> editMemo(@PathVariable Long memoId, @RequestBody Map<String, String> body, Principal principal) {
      log.info("메모 수정 요청: ID={}", memoId);
      routeService.editMemo(memoId, body.get("content"), principal.getName());
      return ResponseEntity.ok("수정했습니다.");
   }

   @DeleteMapping("/memo/{memoId}")
   public ResponseEntity<String> deleteMemo(@PathVariable Long memoId, Principal principal) {
      log.info("메모 삭제 요청: ID={}", memoId);
      return ResponseEntity.ok(routeService.deleteMemo(memoId, principal.getName()));
   }

   @DeleteMapping("/place/{placeId}")
   public ResponseEntity<String> deletePlace(@PathVariable Long placeId, Principal principal) {
      log.info("장소 삭제 요청: ID={}", placeId);
      return ResponseEntity.ok(routeService.deletePlace(placeId, principal.getName()));
   }

   @PatchMapping("/{routeId}/title")
   public ResponseEntity<String> updateTitle(@PathVariable Long routeId, @RequestBody Map<String, String> body, Principal principal) {
      log.info("루트 제목 수정 요청: ID={}, 요청자={}", routeId, principal.getName());
      routeService.updateTitle(routeId, body.get("title"), principal.getName());
      return ResponseEntity.ok("제목이 수정되었습니다.");
   }

   @PatchMapping("/{routeId}/move")
   public ResponseEntity<Collection<BlockDto>> moveBlock(
         @PathVariable Long routeId,
         @RequestBody Map<String, Object> body,
         Principal principal) {
      Long blockId = ((Number) body.get("blockId")).longValue();
      Long targetBlockId = ((Number) body.get("targetBlockId")).longValue();
      boolean insertAfter = (Boolean) body.get("insertAfter");
      log.info("블록 이동 요청: 루트ID={}, 블록ID={}, targetBlockId={}, insertAfter={}", routeId, blockId, targetBlockId, insertAfter);
      return ResponseEntity.ok(routeService.moveBlock(routeId, blockId, targetBlockId, insertAfter, principal.getName()));
   }

   @DeleteMapping("/{routeId}")
   public ResponseEntity<String> deleteRoute(
         @PathVariable Long routeId,
         Principal principal) throws AccessDeniedException {
      log.info("루트 삭제 요청: ID={}, 요청자={}", routeId, principal.getName());
      routeService.deleteRoute(routeId, principal.getName());
      return ResponseEntity.ok("루트를 삭제했습니다.");
   }

   @PostMapping("/copy/{routeId}")
   public ResponseEntity<String> copyRoute(@PathVariable Long routeId, Principal principal) {
      log.info("루트 복사 요청: ID={}, 요청자={}", routeId,  principal.getName());
      User user = userService.findByUsername(principal.getName());
      routeService.addCopiedRoute(routeId, user.getUsername());
      return ResponseEntity.ok("루트를 복사해 새로 생성했습니다.");
   }
}

