package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.security.dto.LoginRequest;
import com.example.project.security.dto.LoginResponse;
import com.example.project.security.dto.RegisterRequest;
import com.example.project.security.jwt.JwtUtil;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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

   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
      userService.register(registerRequest);

      String accessToken = jwtUtil.CreateAccessToken(registerRequest.getUsername());
      Map<String, String> response =new HashMap<>();
      response.put("message", "Registered");
      response.put("token",accessToken);
      return ResponseEntity.ok(response);
   }
   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody LoginRequest request){
      LoginResponse response= userService.login(request);
      return ResponseEntity.ok("Token = "+response);
   }
}
