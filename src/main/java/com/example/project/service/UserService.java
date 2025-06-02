package com.example.project.service;

import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.mapper.UserMapper;
import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.config.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
   private final UserMapper userMapper;
   private final JwtUtil jwtUtil;
   private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
   private final AuthenticationManager authenticationManager;

   public User findByUsername(String username){
      return userMapper.findByUsername(username);
   }
   public void save(User user){
      userMapper.save(user);
   }

   // 회원가입
   public void register(RegisterRequest registerRequest){
      if(!registerRequest.getPassword().equals(registerRequest.getConfirmedPassword())){
         throw new RuntimeException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      }

      User user = findByUsername(registerRequest.getUsername());
      if(user!= null){
         throw new RuntimeException("이미 존재하는 아이디입니다.");
      }


      User newUser = new User();
      newUser.setUsername(registerRequest.getUsername());
      String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
      newUser.setPassword(encodedPassword);
      newUser.setEmail(registerRequest.getEmail());
      newUser.setRole("ROLE_USER");
      save(newUser);
   }

   // 로그인
   public LoginResponse login(LoginRequest loginRequest){
      User user = findByUsername(loginRequest.getUsername());
      if(user==null){
         throw new RuntimeException("User not found.");
      }

      Authentication authentication =authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
      );


      String accessToken = jwtUtil.CreateAccessToken(authentication.getName());
      return new LoginResponse(accessToken);
   }

   // 알림 조회
   public List<Notification> notification(String username){
      User user = userMapper.findByUsername(username);
      if(user==null){
         throw new UsernameNotFoundException(username+"을 찾을 수 없습니다.");
      }
      List<Notification> notifications= userMapper.findNotificationsByUserId(user.getId());
      return notifications;
   }
}
