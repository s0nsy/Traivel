package com.example.project.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
public class JwtUtil {
   //   유지 보수 시
   @Value("${jwt.secret}")
   private String secret;

   @Value("${jwt.expiration}")
   private long AccessTokenExpireTime;

   public String CreateAccessToken(String userId){
      return CreateToken(userId,AccessTokenExpireTime);
   }

   @PostConstruct
   protected void init(){
      this.secret= Base64.getEncoder().encodeToString(secret.getBytes());
   }
   // 토큰 생성
   public String CreateToken(String userId, long expireTime){
      Date now = new Date();
      return Jwts.builder()
            .setSubject(userId) // 사용자 식별자. 페이로드의 'sub'
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime()+expireTime))
            .signWith(SignatureAlgorithm.HS256, secret) //서명
            .compact(); //최종적으로 jwt 문자열 생성
   }
   // 토큰 검증
   public boolean validateToken(String token){
      try{
         Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
         return true;
      }catch(JwtException|IllegalArgumentException e){
         return false;
      }
   }

   // 요청에서 토큰 추출
   public String resolveToken(HttpServletRequest request){
      String bearer = request.getHeader("Authorization");
      if(bearer != null&&bearer.startsWith("Bearer ")){
         return bearer.substring(7);
      }
      return null;
   }

   // 토큰에서 사용자 추출
   public String getUserIdFromToken(String token){
      Claims claims = Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .getBody(); //claim(객체 내용) 반환.
      return claims.getSubject();
   }

}
