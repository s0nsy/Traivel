package com.example.project.service;

import com.example.project.config.NaverApiConfig;
import com.example.project.entity.dto.NaverSearchListResponse;
import com.example.project.entity.dto.NaverSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class NaverSearchService {
   private final NaverApiConfig naverApiConfig;
   private final WebClient naverWebClient;
   private final WebClient naverMapClient;

   // 네이버 검색 API로 장소 검색
   public List<NaverSearchRequest> searchPlace(String keyword){
      String clientId = naverApiConfig.getSearchId();
      String clientSecret = naverApiConfig.getSearchSecret();

      String uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/local.json")
            .queryParam("query", keyword)
            .queryParam("display", 10)
            .queryParam("sort", "random")
            .build()
            .toUriString();
      System.out.println(uri);
      return naverWebClient.get()
            .uri(uri)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .retrieve()
            .bodyToMono(NaverSearchListResponse.class)
            .map(NaverSearchListResponse::getItems)
            .block();
   }

}
