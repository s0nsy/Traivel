package com.example.project.controller;

import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
   private final JwtUtil jwtUtil;
   private final UserService userService;

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
}
