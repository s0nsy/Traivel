package com.example.project.service;

import com.example.project.config.NaverApiConfig;
import com.example.project.entity.Place;
import com.example.project.entity.PlaceItem;
import com.example.project.entity.Route;
import com.example.project.entity.dto.AddPlaceToRouteRequest;
import com.example.project.entity.dto.NaverSearchListResponse;
import com.example.project.entity.dto.NaverSearchRequest;
import com.example.project.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class NaverSearchService {
   private final NaverApiConfig naverApiConfig;
   private final WebClient naverWebClient;
   private final WebClient naverMapClient;
   private final PlaceMapper placeMapper;

   public List<NaverSearchRequest> searchPlace(String keyword){
      String clientId = naverApiConfig.getSearchId();
      String clientSecret = naverApiConfig.getSearchSecret();

      String uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/local.json")
            .queryParam("query", keyword)
            .queryParam("display", 5)
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

   public void addPin(AddPlaceToRouteRequest request){
//      Place place = placeMapper.findByPlaceTitle(request.getNaverSearchRequest().getTitle());
      NaverSearchRequest place = request.getNaverSearchRequest();

      Place newPlace = new Place();
      newPlace.setTitle(place.getTitle());
      newPlace.setAddress(place.getAddress());
      newPlace.setRoadAddress(place.getRoadAddress());
      newPlace.setLink(place.getLink());
      newPlace.setCategory(place.getCategory());
      newPlace.setMapx(place.getMapx());
      newPlace.setMapy(place.getMapy());

      Route route = placeMapper.findByRouteId(request.getRouteId());
      if(route==null)
         throw new RuntimeException("루트가 존재하지 않습니다.");

      newPlace.setRoute(route);
      placeMapper.save(newPlace);

      PlaceItem placeItem = new PlaceItem();
      placeItem.setPlace(newPlace);
      placeItem.setRoute(route);
      placeItem.setDay(request.getDay());

      //order: 요청에 있으면 그대로, 없으면 마지막에 추가
      Map<String, Object> map = new HashMap<>();

      map.put("routeId", request.getRouteId());
      map.put("day",request.getDay());

      int maxOrder = placeMapper.findMaxOrderByRouteDay(map);
      placeItem.setOrder(maxOrder+1);
      placeMapper.saveRouteItem(placeItem);
      placeMapper.savePlaceItem(placeItem);
      System.out.println(placeItem);

   }

   public String reverseGeocode(Map<String, Double> coords){
      double latitude= coords.get("lat");
      double longitude = coords.get("lng");
      String coord = latitude+ ","+longitude;

      String response = naverMapClient.get()
            .uri(uriBuilder -> uriBuilder
                  .queryParam("coord", coord)
                  .queryParam("output","json")
                  .queryParam("orders", "addr,roadaddr")
                  .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();

      return response;
   }
}
