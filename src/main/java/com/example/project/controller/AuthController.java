package com.example.project.controller;

import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.entity.dto.NotificationDto;
import com.example.project.mapper.UserMapper;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
   private final JwtUtil jwtUtil;
   private final UserService userService;
   private final UserMapper userMapper;

   // 회원가입
   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
      userService.register(registerRequest);

      String accessToken = jwtUtil.CreateAccessToken(registerRequest.getUsername());
      Map<String, String> response =new HashMap<>();
      response.put("message", "Registered");
      response.put("token",accessToken);
      return ResponseEntity.ok(response);
   }

   // 로그인
   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody LoginRequest request){
      LoginResponse response= userService.login(request);
      return ResponseEntity.ok("Token = "+response);
   }

   // 알림 조회
   @GetMapping("/notifications")
   public ResponseEntity<List<Notification>> notifications(@AuthenticationPrincipal UserDetails userDetails){
      List<Notification> notifications= userService.notification(userDetails.getUsername());
      return ResponseEntity.ok(notifications);
   }
   // 초대 알림 전송
   @PostMapping("/invite")
   public ResponseEntity<String> invite(String username,@AuthenticationPrincipal UserDetails userDetails) throws AuthenticationException, AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      if(user==null)
         throw new AccessDeniedException("해당 루트 관리자가 아닙니다.");
      Notification newNotification = new Notification();
      User invitedUser = userMapper.findByUsername(username);
      if(invitedUser==null)
         throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
      newNotification.setUserId(invitedUser.getId());
      String message = userDetails.getUsername()+"님이 여행 일정에 초대했습니다!";
      newNotification.setMessage(message);
      newNotification.setType("INVITE");
      newNotification.setCreatedAt(LocalDateTime.now());
      userMapper.addNotification(newNotification);
      return ResponseEntity.ok("알림 추가했습니다.");
   }

   // 알림 전송
   @PostMapping("/notifications")
   public ResponseEntity<String> addNotification(Long userId, @RequestBody NotificationDto notification, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      if(!user.getRole().equals("ADMIN"))
         throw new AccessDeniedException("관리자가 아닙니다.");
      Notification newNotification = new Notification();
      newNotification.setUserId(userId);
      newNotification.setMessage(notification.getMessage());
      newNotification.setType(notification.getType());
      newNotification.setRead(notification.isRead());
      newNotification.setCreatedAt(LocalDateTime.now());
      userMapper.addNotification(newNotification);
      return ResponseEntity.ok("알림 추가했습니다.");
   }
}
