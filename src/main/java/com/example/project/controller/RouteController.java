package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.dto.*;
import com.example.project.service.RouteService;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteController {
   private final RouteService routeService;
   private final UserService userService;

   @PostMapping
   public Long addRoute(Principal principal, String title) {
      log.info("루트 추가 요청: 제목='{}', 사용자={}", title, principal.getName());
      Long routeId = routeService.addRoute(principal.getName(), title);
      return routeId;
   }

   @GetMapping("/{routeId}")
   public ResponseEntity<Collection<BlockDto>> getRoute(@PathVariable Long routeId) {
      log.debug("루트 조회: ID={}", routeId);
      return ResponseEntity.ok(routeService.getAllBlocks(routeId));
   }

   @PostMapping("/memo")
   public ResponseEntity<String> addMemo(@Valid @RequestBody MemoRequest memoRequest) {
      log.info("메모 추가 요청: 루트ID={}, day={}", memoRequest.getRouteId(), memoRequest.getDay());
      routeService.addMemo(memoRequest);
      return ResponseEntity.ok("메모를 추가했습니다.");
   }

   @PutMapping("/memo/{memoId}")
   public ResponseEntity<String> editMemo(@PathVariable Long memoId, @RequestBody Map<String, String> body) {
      log.info("메모 수정 요청: ID={}", memoId);
      routeService.editMemo(memoId, body.get("content"));
      return ResponseEntity.ok("수정했습니다.");
   }

   @DeleteMapping("/memo/{MemoId}")
   public ResponseEntity<String> deleteMemo(@PathVariable Long MemoId) {
      log.info("메모 삭제 요청: ID={}", MemoId);
      return ResponseEntity.ok(routeService.deleteMemo(MemoId));
   }

   @DeleteMapping("/place/{placeId}")
   public ResponseEntity<String> deletePlace(@PathVariable Long placeId) {
      log.info("장소 삭제 요청: ID={}", placeId);
      return ResponseEntity.ok(routeService.deletePlace(placeId));
   }

   @DeleteMapping("/{routeId}")
   public ResponseEntity<String> deleteRoute(
         @PathVariable Long routeId,
         @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      log.info("루트 삭제 요청: ID={}, 요청자={}", routeId, userDetails.getUsername());
      User user = userService.findByUsername(userDetails.getUsername());
      routeService.deleteRoute(routeId, user);
      return ResponseEntity.ok("루트를 삭제했습니다.");
   }
}
