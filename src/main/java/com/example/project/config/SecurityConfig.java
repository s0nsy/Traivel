package com.example.project.config;

import com.example.project.security.jwt.JwtAuthenticationFilter;
import com.example.project.security.jwt.JwtAuthorizationFilter;
import com.example.project.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@EnableWebSecurity: 웹 보안 구성할 때 사용. WebSecurityConfigurerAdapter 클래스를 상속받은 설정 클래스를 등록
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   private final JwtUtil jwtUtil;
   private final UserDetailsService userDetailsService;
   @Bean
   public JwtAuthenticationFilter jwtAuthenticationFilter() {
      return new JwtAuthenticationFilter(jwtUtil);
   }

   @Bean
   public JwtAuthorizationFilter jwtAuthorizationFilter() {
      return new JwtAuthorizationFilter(jwtUtil);
   }
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // HttpSecurity 설정
      http
            .authorizeHttpRequests(auth->auth
            .requestMatchers("/login", "/signup").permitAll()  // 로그인, 회원가입 페이지는 모두에게 열어줌
            .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 인가 필터 추가

      return http.build();
   }


}

