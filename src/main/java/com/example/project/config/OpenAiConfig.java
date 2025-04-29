package com.example.project.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfig {
   @Value("${spring.ai.openai.api-key}")
   private String apiKey;

   //OpenAI 서버랑 통신할 때 HTTP 요청을 보냄
   @Bean
   public WebClient webClient(){
      return WebClient.builder()
              .baseUrl("https://api.openai.com/v1/")
              .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+apiKey).build();
   }
}
