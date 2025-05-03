package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.entity.dto.*;
import com.example.project.mapper.PlaceMapper;
import com.example.project.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalTime.now;

@RequiredArgsConstructor
@Service
public class RouteService {
   private final UserMapper userMapper;
   private final PlaceMapper placeMapper;
   // 루트 추가
   public void addRoute(String username, String title){
      User user = userMapper.findByUsername(username);
      if(user==null)
         throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

      Route route = new Route();
      route.setTitle(title);
      route.setCreated_at(LocalDateTime.now());
      route.getUsers().add(user);

      placeMapper.saveRoute(route);
   }

   // 장소 추가
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
      placeMapper.savePlace(newPlace);

      PlaceItem placeItem = new PlaceItem();
      placeItem.setPlace(newPlace);
      placeItem.setRoute(route);
      placeItem.setDay(request.getDay());

      Map<String, Object> map = new HashMap<>();
      //여러값(routeId,day) 넘겨야하니 map
      map.put("routeId", request.getRouteId());
      map.put("day",request.getDay());

      int maxOrder = placeMapper.findMaxOrderByRouteDay(map);
      placeItem.setOrder(maxOrder+1);
      placeMapper.saveRouteItem(placeItem);
      placeMapper.savePlaceItem(placeItem);
      System.out.println(placeItem);

   }

   // 메모 추가
   public void addMemo(MemoRequest memoRequest){

      Memo memo = new Memo();
      memo.setContent(memoRequest.getContent());
      Route route = placeMapper.findByRouteId(memoRequest.getRouteId());
      memo.setRoute(route);
      placeMapper.saveMemo(memo);

      MemoItem memoItem = new MemoItem();
      memoItem.setMemo(memo);
      memoItem.setRoute(route);
      memoItem.setDay(memoRequest.getDay());

      Map<String, Object> map = new HashMap<>();
      map.put("routeId", memoRequest.getRouteId());
      map.put("day", memoRequest.getDay());

      int maxOrder = placeMapper.findMaxOrderByRouteDay(map);
      memoItem.setOrder(maxOrder+1);
      placeMapper.saveRouteItem(memoItem);
      placeMapper.saveMemoItem(memoItem);
   }

   // 메모 수정
   public void editMemo(MemoEditRequest request){
      Memo memo = placeMapper.findByMemoId(request.getId());
      memo.setContent(request.getContent());
      placeMapper.editMemo(memo);
   }

   // 메모 삭제
   public void deleteMemo(DeleteRequest deleteRequest){
      MemoItem memoItem= placeMapper.findByMemoItemId(deleteRequest.getRouteItemId());

      placeMapper.deleteMemoItem(memoItem);
      placeMapper.deleteRouteItem(deleteRequest.getRouteItemId());
   }

   // 장소 삭제
   public void deletePlace(DeleteRequest deleteRequest) {
      PlaceItem placeItem= placeMapper.findByPlaceItemId(deleteRequest.getRouteItemId());

      placeMapper.deletePlaceItem(placeItem);
      placeMapper.deleteRouteItem(deleteRequest.getRouteItemId());
   }

   // 여행 루트 전체 삭제


}
