package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.security.dto.LoginRequest;
import com.example.project.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {
   private final JwtUtil jwtUtil;
   private final UserRepository userRepository;

   @PostMapping
   public ResponseEntity<?> login(@RequestBody LoginRequest request){
      User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(()-> new RuntimeException("User not found"));

      if(!user.getPassword().equals(request.getPassword())){
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
      }

      String accessToken = jwtUtil.CreateAccessToken(user.getUsername());
      String refreshToken = jwtUtil.CreateRefreshToken(user.getUsername());

      return ResponseEntity.ok()
            .header("Authorization","Bearer"+accessToken)
            .body("로그인 성공");
   }
}
