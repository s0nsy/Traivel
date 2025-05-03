package com.example.project.config;

import com.example.project.config.security.jwt.JwtTokenFilter;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@EnableWebSecurity: 웹 보안 구성할 때 사용. WebSecurityConfigurerAdapter 클래스를 상속받은 설정 클래스를 등록
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   private final JwtUtil jwtUtil;
   private final CustomUserDetailsService customUserDetailsService;

   @Bean
   public JwtTokenFilter jwtTokenFilter(){
      return new JwtTokenFilter(jwtUtil, customUserDetailsService);
   }
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // HttpSecurity 설정
      http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth->auth
            .requestMatchers("/auth/**", "/register",
                  "/swagger-ui/**",
                  "/swagger-resources/**",
                  "/v3/api-docs/**",
                  "/**"
            )
                        .permitAll()  // 로그인, 회원가입 페이지는 모두에게 열어줌
            .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            )
            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 인가 필터 추가

      return http.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
      return authenticationConfiguration.getAuthenticationManager();
   }
}

