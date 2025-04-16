package com.example.project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
   //OncePerRequestFilter: Http Request 한 번의 요청에 대해 한 번만 실행하는 Filter

   private final JwtUtil jwtUtil;


   @Override
   protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
         throws ServletException, IOException{

      String token = jwtUtil.resolveToken(request);

      if(token!=null&& jwtUtil.validateToken(token)){
         Authentication auth =jwtUtil.getAuthentication(token);
         SecurityContextHolder.getContext().setAuthentication(auth);
      }
      filterChain.doFilter(request, response);
   }

}
