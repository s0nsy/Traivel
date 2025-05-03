package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.dto.DeleteRequest;
import com.example.project.entity.dto.MemoEditRequest;
import com.example.project.entity.dto.MemoRequest;
import com.example.project.mapper.UserMapper;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteController {
   private final RouteService routeService;
   private final UserMapper userMapper;

   // 루트 db 추가
   @PostMapping("/addRoute")
   public ResponseEntity<String> addRoute(@AuthenticationPrincipal UserDetails user, String title){
      routeService.addRoute(user.getUsername(),title);
      return ResponseEntity.ok("루트를 추가했습니다.");
   }

   // 메모 추가
   @PostMapping("/addMemo")
   public ResponseEntity<String> addMemo(@RequestBody MemoRequest memoRequest){
      routeService.addMemo(memoRequest);
      return ResponseEntity.ok("메모를 추가했습니다.");
   }

   // 메모 수정
   @PutMapping("/edit")
   public ResponseEntity<String> editMemo(@RequestBody MemoEditRequest memoRequest){
      routeService.editMemo(memoRequest);
      return ResponseEntity.ok("수정했습니다.");
   }

   // 메모 삭제
   @DeleteMapping("/deleteMemo")
   public ResponseEntity<String> deleteMemo(@RequestBody DeleteRequest deleteRequest){
      routeService.deleteMemo(deleteRequest);
      return ResponseEntity.ok("삭제했습니다.");
   }

   // 장소 삭제
   @DeleteMapping("/deletePlace")
   public ResponseEntity<String> deletePlace(@RequestBody DeleteRequest deleteRequest){
      routeService.deletePlace(deleteRequest);
      return ResponseEntity.ok("삭제했습니다.");
   }
}
