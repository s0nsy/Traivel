package com.example.project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

   private final JwtUtil jwtUtil;

   @Override
   protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
         throws ServletException, IOException {
      Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

      if(authentication !=null&&authentication.isAuthenticated()){
         if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
            return;
         }
      }
      filterChain.doFilter(request,response);
   }
}
