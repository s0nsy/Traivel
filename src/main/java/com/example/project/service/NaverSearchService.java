package com.example.project.service;

import com.example.project.entity.dto.NaverImageSearchResponse;
import com.example.project.entity.dto.NaverPlaceDto;
import com.example.project.entity.dto.NaverSearchListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class NaverSearchService {
    private final WebClient naverSearchWebClient;

    public NaverSearchService(@Qualifier("naverSearchWebClient") WebClient naverSearchWebClient) {
        this.naverSearchWebClient = naverSearchWebClient;
    }

    public List<NaverPlaceDto> searchPlace(String keyword) {
        log.info("네이버 장소 검색: keyword='{}'", keyword);
        NaverSearchListResponse response = naverSearchWebClient.get()
                .uri(builder -> builder
                        .path("/search/v1/local")
                        .queryParam("query", keyword)
                        .queryParam("display", 10)
                        .build())
                .retrieve()
                .bodyToMono(NaverSearchListResponse.class)
                .block();

        if (response == null || response.getItems() == null) return List.of();

        log.info("네이버 장소 검색 결과: keyword='{}', 결과 수={}", keyword, response.getItems().size());
        return response.getItems();
    }

    public String searchImageThumbnail(String keyword) {
        try {
            NaverImageSearchResponse response = naverSearchWebClient.get()
                    .uri(builder -> builder
                            .path("/search/v1/image")
                            .queryParam("query", keyword)
                            .queryParam("display", 1)
                            .build())
                    .retrieve()
                    .bodyToMono(NaverImageSearchResponse.class)
                    .block();

            if (response == null || response.getItems() == null || response.getItems().isEmpty()) return null;
            return response.getItems().get(0).getThumbnail();
        } catch (Exception e) {
            log.warn("이미지 검색 실패: keyword='{}', {}", keyword, e.getMessage());
            return null;
        }
    }
}
