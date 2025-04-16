package com.example.project.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
   private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
   private final long AccessTokenExpireTime = 1000*60*60*2;
   private final long RefreshTokenExpireTime = 1000*60*60*24*7;
   //   유지 보수 시
//   @Value("${jwt.secret}")
//   private String secret;
//
//   @Value("${jwt.access-token-expire-time}")
//   private long ACCESS_TOKEN_EXPIRE_TIME;
//
//   @Value("${jwt.refresh-token-expire-time}")
//   private long REFRESH_TOKEN_EXPIRE_TIME;

   public String CreateAccessToken(String userId){
      return CreateToken(userId,AccessTokenExpireTime);
   }

   public String CreateRefreshToken(String userId){
      return CreateToken(userId, RefreshTokenExpireTime);
   }
   // 토큰 생성
   public String CreateToken(String userId, long expireTime){
      Date now = new Date();
      return Jwts.builder()
            .setSubject(userId) // 사용자 식별자. 페이로드의 'sub'
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime()+expireTime))
            .signWith(key) //서명
            .compact(); //최종적으로 jwt 문자열 생성
   }
   // 토큰 검증
   public boolean validateToken(String token){
      try{
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      }catch(JwtException|IllegalArgumentException e){
         return false;
      }
   }
   // Authenticaton 객체 생성
   public Authentication getAuthentication(String token) {
      String username = getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
      List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

      UserDetails userDetails = new User(username, "", authorities);
      return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
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
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody(); //claim(객체 내용) 반환.
      return claims.getSubject();
   }

}
