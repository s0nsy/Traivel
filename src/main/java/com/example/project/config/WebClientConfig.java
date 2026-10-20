package com.example.project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

   private final OpenAiConfig openAiConfig;
   private final NaverApiConfig naverApiConfig;

   @Bean
   public WebClient OpenAiWebClient(){
      return WebClient.builder()
            .baseUrl("https://api.openai.com/v1/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiConfig.getApiKey())
            .build();
   }

   @Bean
   public WebClient naverSearchWebClient() {
      ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(c -> c.defaultCodecs()
                  .jackson2JsonDecoder(new Jackson2JsonDecoder(
                        new ObjectMapper(),
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_PLAIN)))
            .build();
      return WebClient.builder()
            .baseUrl("https://naverapihub.apigw.ntruss.com")
            .defaultHeader("X-NCP-APIGW-API-KEY-ID", naverApiConfig.getSearchId())
            .defaultHeader("X-NCP-APIGW-API-KEY", naverApiConfig.getSearchSecret())
            .exchangeStrategies(strategies)
            .build();
   }

   @Bean
   public WebClient naverMapClient() {
      return WebClient.builder()
            .defaultHeader("X-NCP-APIGW-API-KEY-ID", naverApiConfig.getMapId())
            .defaultHeader("X-NCP-APIGW-API-KEY", naverApiConfig.getMapSecret())
            .build();
   }
}
