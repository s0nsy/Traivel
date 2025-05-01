package com.example.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

   private final OpenAiConfig openAiConfig;
   private final NaverApiConfig naverApiConfig;

   //OpenAI 서버랑 통신할 때 HTTP 요청을 보냄
   @Bean
   public WebClient OpenAiWebClient(){
      return WebClient.builder()
            .baseUrl("https://api.openai.com/v1/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+openAiConfig.getApiKey()).build();
   }

   @Bean
   public WebClient naverWebClient(){
      return WebClient.builder()
            .baseUrl(("https://openapi.naver.com"))
            .defaultHeader("X-Naver-Client-Id", naverApiConfig.getSearchId())
            .defaultHeader("X-Naver-Client-Server", naverApiConfig.getSearchSecret())
            .build();
   }
}
