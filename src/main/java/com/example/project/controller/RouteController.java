package com.example.project.controller;

import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Route;
import com.example.project.entity.User;
import com.example.project.entity.dto.*;
import com.example.project.mapper.PlaceMapper;
import com.example.project.mapper.UserMapper;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/route")
public class RouteController {
   private final RouteService routeService;
   private final UserMapper userMapper;
   private final JwtUtil jwtUtil;
   private final SimpMessagingTemplate messagingTemplate;
   private final PlaceMapper placeMapper;

   // 루트 db 추가
   @PostMapping("/")
   public ResponseEntity<String> addRoute(@AuthenticationPrincipal UserDetails user, String title){
      routeService.addRoute(user.getUsername(),title);
      return ResponseEntity.ok("루트를 추가했습니다.");
   }

   // 루트 조회
   @GetMapping("/{id}")
   public ResponseEntity<Collection<BlockDto>> getRoute(@PathVariable Long id){
      return ResponseEntity.ok(routeService.getAllBlocks(id));
   }

   // 메모 추가
   @PostMapping("/memo")
   public ResponseEntity<String> addMemo(@RequestBody MemoRequest memoRequest){
      routeService.addMemo(memoRequest);
      return ResponseEntity.ok("메모를 추가했습니다.");
   }

   // 메모 수정
   @PutMapping("/memo/{id}")
   public ResponseEntity<String> editMemo(@PathVariable Long id, String content){
      routeService.editMemo(id,content);
      return ResponseEntity.ok("수정했습니다.");
   }

   // 메모 삭제
   @DeleteMapping("/memo/{id}")
   public ResponseEntity<String> deleteMemo(@PathVariable Long id){
      routeService.deleteMemo(id);
      return ResponseEntity.ok("삭제했습니다.");
   }

   // 장소 삭제
   @DeleteMapping("/place/{id}")
   public ResponseEntity<String> deletePlace(@PathVariable Long id){
      routeService.deletePlace(id);
      return ResponseEntity.ok("삭제했습니다.");
   }

   // 루트 삭제
   @DeleteMapping("/route/{id}")
   public ResponseEntity<String> deleteRoute(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      routeService.deleteRoute(id,user);
      return ResponseEntity.ok("루트를 삭제했습니다.");
   }

   // 초대 링크 생성
   @PostMapping("/link")
   public ResponseEntity<String> createLink(Long routeId, @RequestHeader("Authorization") String auth) throws AccessDeniedException {
      String token = auth.replace("Bearer ","");
      String username= jwtUtil.getUserIdFromToken(token);
      String linkToken =routeService.createInviteLink(routeId, username);
      return ResponseEntity.ok(linkToken);
   }

   // 링크를 통해 여행 루트 입장
   @GetMapping("/invite/{token}")
   public ResponseEntity<String> joinRoute(@PathVariable String token,@AuthenticationPrincipal UserDetails userDetails){
      User user = userMapper.findByUsername(userDetails.getUsername());
      routeService.joinRoute(token,user);
      return ResponseEntity.ok("정상적으로 초대되었습니다.");
   }

   // 멤버 방출
   @DeleteMapping("/member")
   public ResponseEntity<String> deleteMember(Long routeId, @AuthenticationPrincipal UserDetails userDetails, String username) throws AccessDeniedException {
      User member = userMapper.findByUsername(username);
      routeService.deleteMember(routeId,userDetails.getUsername(),member);
      return ResponseEntity.ok(username+ "을 퇴장시켰습니다.");
   }

   // 루트 공유
   @MessageMapping("/move-block")
   public void moveBlock(BlockMoveMessage message){
      System.out.println("블록 이동 요청 받음: " + message.getBlockId() + " -> " + message.getNewOrder());
      BlockDto updated=routeService.moveBlock(message.getRouteId(),message.getBlockId(),message.getNewOrder(), message.getNewDay());
      if(updated!=null){
         messagingTemplate.convertAndSend("/topic/blocks", routeService.getAllBlocks(message.getRouteId()));
      }
   }

}
