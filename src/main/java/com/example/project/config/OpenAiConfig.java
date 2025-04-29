package com.example.project.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;

@Configuration
public class OpenApiConfig {

   @Bean
   public RestTemplate restTemplate() {
      RestTemplate restTemplate = new RestTemplate();
      return restTemplate;
   }

   @Bean
   public HttpHeaders headers() {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth()
   }
   @Bean
   ChatClient chatClient(ChatClient.Builder builder){
      return builder.defaultSystem(prompt).build();
   }

   String prompt="";
}
