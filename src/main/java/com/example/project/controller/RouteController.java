package com.example.project.controller;

import com.example.project.entity.Route;
import com.example.project.entity.User;
import com.example.project.entity.dto.*;
import com.example.project.mapper.UserMapper;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteController {
   private final RouteService routeService;
   private final UserMapper userMapper;

   // 루트 db 추가
   @PostMapping("/")
   public Long addRoute(Principal principal, String title){
      Long routeId = routeService.addRoute(principal.getName(),title);
      return routeId;
   }

   // 루트 조회
   @GetMapping("/{routeId}")
   public ResponseEntity<Collection<BlockDto>> getRoute(@PathVariable Long routeId){
      return ResponseEntity.ok(routeService.getAllBlocks(routeId));
   }

   // 메모 추가
   @PostMapping("/memo")
   public ResponseEntity<String> addMemo(@RequestBody MemoRequest memoRequest){
      routeService.addMemo(memoRequest);
      return ResponseEntity.ok("메모를 추가했습니다.");
   }

   // 메모 수정
   @PutMapping("/memo/{memoId}")
   public ResponseEntity<String> editMemo(@PathVariable Long memoId, String content){
      routeService.editMemo(memoId,content);
      return ResponseEntity.ok("수정했습니다.");
   }

   // 메모 삭제
   @DeleteMapping("/memo/{MemoId}")
   public ResponseEntity<String> deleteMemo(@PathVariable Long MemoId){
      String response = routeService.deleteMemo(MemoId);
      return ResponseEntity.ok(response);
   }

   // 장소 삭제
   @DeleteMapping("/place/{placeId}")
   public ResponseEntity<String> deletePlace(@PathVariable Long placeId){
      String response =routeService.deletePlace(placeId);
      return ResponseEntity.ok(response);
   }

   // 루트 삭제
   @DeleteMapping("/{routeId}")
   public ResponseEntity<String> deleteRoute(@PathVariable Long routeId, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      routeService.deleteRoute(routeId,user);
      return ResponseEntity.ok("루트를 삭제했습니다.");
   }



}
