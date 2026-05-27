package com.example.project.service;

import com.example.project.config.NaverApiConfig;
import com.example.project.entity.dto.NaverPlaceDto;
import com.example.project.entity.dto.NaverSearchListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverSearchService {
   private final NaverApiConfig naverApiConfig;
   private final WebClient naverWebClient;
   private final WebClient naverMapClient;

   public List<NaverPlaceDto> searchPlace(String keyword) {
      log.info("네이버 장소 검색: keyword='{}'", keyword);
      String uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/local.json")
            .queryParam("query", keyword)
            .queryParam("display", 10)
            .queryParam("sort", "random")
            .build()
            .toUriString();

      List<NaverPlaceDto> results = naverWebClient.get()
            .uri(uri)
            .header("X-Naver-Client-Id", naverApiConfig.getSearchId())
            .header("X-Naver-Client-Secret", naverApiConfig.getSearchSecret())
            .retrieve()
            .bodyToMono(NaverSearchListResponse.class)
            .map(NaverSearchListResponse::getItems)
            .block();

      log.info("네이버 장소 검색 결과: keyword='{}', 결과 수={}", keyword, results != null ? results.size() : 0);
      return results;
   }
}
