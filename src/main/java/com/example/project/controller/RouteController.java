package com.example.project.controller;

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
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteController {
   private final RouteService routeService;
   private final UserMapper userMapper;

   // 루트 db 추가
   @PostMapping("/")
   public ResponseEntity<String> addRoute(@AuthenticationPrincipal UserDetails user, String title){
      routeService.addRoute(user.getUsername(),title);
      return ResponseEntity.ok("루트를 추가했습니다.");
   }

   // 루트 조회
   @GetMapping("/{id}")
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
   @PutMapping("/memo/{id}")
   public ResponseEntity<String> editMemo(@PathVariable Long memoId, String content){
      routeService.editMemo(memoId,content);
      return ResponseEntity.ok("수정했습니다.");
   }

   // 메모 삭제
   @DeleteMapping("/memo/{id}")
   public ResponseEntity<String> deleteMemo(@PathVariable Long MemoId){
      routeService.deleteMemo(MemoId);
      return ResponseEntity.ok("삭제했습니다.");
   }

   // 장소 삭제
   @DeleteMapping("/place/{id}")
   public ResponseEntity<String> deletePlace(@PathVariable Long placeId){
      routeService.deletePlace(placeId);
      return ResponseEntity.ok("삭제했습니다.");
   }

   // 루트 삭제
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteRoute(@PathVariable Long routeId, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      routeService.deleteRoute(routeId,user);
      return ResponseEntity.ok("루트를 삭제했습니다.");
   }



}
