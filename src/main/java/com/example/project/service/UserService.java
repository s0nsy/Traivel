package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.mapper.UserMapper;
import com.example.project.security.dto.LoginRequest;
import com.example.project.security.dto.LoginResponse;
import com.example.project.security.dto.RegisterRequest;
import com.example.project.security.jwt.JwtUtil;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
   private final UserMapper userMapper;
   private final JwtUtil jwtUtil;

   public User findByUsername(String username){
      return userMapper.findByUsername(username);
   }
   public User save(User user){
      return userMapper.save(user);
   }
   public User register(RegisterRequest registerRequest){
      User user = findByUsername(registerRequest.getUsername());
      if(user!= null){
         throw new RuntimeException("이미 존재하는 아이디입니다.");
      }
      if(registerRequest.getPassword().equals(registerRequest.getConfirmedPassword())){
         throw new RuntimeException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      }

      User newUser = new User();
      newUser.setUsername(registerRequest.getUsername());
//      String encodedPassword = passwordEncoder.encode(user.getPassword());
//      newUser.setPassword(encodedPassword);
      newUser.setPassword(registerRequest.getPassword());
      newUser.setEmail(registerRequest.getEmail());
      return save(newUser);
   }

   public LoginResponse login(LoginRequest loginRequest){
      User user = findByUsername(loginRequest.getUsername());
      if(user==null){
         throw new RuntimeException("User not found.");
      }

      if(!user.getPassword().equals(loginRequest.getPassword())){
         throw new RuntimeException("Invalid password");
      }

      String accessToken = jwtUtil.CreateAccessToken(user.getUsername());
      String refreshToken = jwtUtil.CreateRefreshToken(user.getUsername());
      return new LoginResponse(accessToken);
   }
}
