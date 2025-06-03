package com.example.project.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
public class NaverApiConfig {
   @Value("${naver.search.id}")
   private String searchId;

   @Value("${naver.search.secret}")
   private String searchSecret;

   @Value("${naver.map.id}")
   private String mapId;

   @Value("${naver.map.secret}")
   private String mapSecret;

}
