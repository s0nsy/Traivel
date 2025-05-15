package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.entity.dto.*;
import com.example.project.mapper.PlaceMapper;
import com.example.project.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
      route.setOwner(user);
      placeMapper.addRoute(route);
      placeMapper.addRouteUser(route,user);
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
   public void deleteRoute(Long routeId, User user) throws AccessDeniedException {
      Route route = placeMapper.findByRouteId(routeId);
      if(route==null)
         throw new NullPointerException("존재하지 않는 루트입니다.");
      Long ownerId = placeMapper.findByOwnerIdByRouteId(routeId);
      if(ownerId!=user.getId())
         throw new AccessDeniedException("해당 여행 루트 생성자가 여행 루트를 삭제할 수 있습니다.");
      placeMapper.deleteRouteUser(route,user);
      placeMapper.deleteRoute(route);
   }

   // 초대 링크 생성
   public String createInviteLink(Long routeId, String username) throws AccessDeniedException {
      Route route = placeMapper.findByRouteId(routeId);
      if(route==null)
         throw new NullPointerException("존재하지 않는 루트입니다.");
      Long ownerId = placeMapper.findByOwnerIdByRouteId(route.getId());
      User user = userMapper.findByUsername(username);
      if(ownerId!=user.getId())
         throw new AccessDeniedException("해당 여행 루트 생성자가 여행 루트를 삭제할 수 있습니다.");
      String token = UUID.randomUUID().toString();
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime expiredAt= now.plusDays(1);

      Invite invite = new Invite();
      invite.setRoute_id(routeId);
      invite.setToken(token);
      invite.setExpired_at(expiredAt);
      placeMapper.saveInvite(invite);

      return "https://도메인.com/invite/" +token;
   }

   // 여행 루트 입장
   public void joinRoute(String token,User user){
      Invite invite = placeMapper.findByToken(token);
      System.out.println(invite);
      System.out.println("routeId: " + invite.getRoute_id());
      Route route = placeMapper.findByRouteId(invite.getRoute_id());
      List<User> users= placeMapper.findUsersByRouteId(route.getId());
      System.out.println(user);

      if(!users.contains(user)) {
         route.getUsers().add(user);
         placeMapper.addRouteUser(route,user);
      }
   }

   // 멤버 방출
   public void deleteMember(Long routeId, String username, User member) throws AccessDeniedException {
      Route route = placeMapper.findByRouteId(routeId);
      if(route==null)
         throw new NullPointerException("존재하지 않는 루트입니다.");
      Long ownerId = placeMapper.findByOwnerIdByRouteId(route.getId());
      User user = userMapper.findByUsername(username);
      if(ownerId!=user.getId())
         throw new AccessDeniedException("루트 관리자가 아닙니다. 멤버를 방출시키실 수 없습니다.");
      List<User> users= placeMapper.findUsersByRouteId(routeId);
      if(users.contains(member)) {
         users.remove(member);
         placeMapper.deleteRouteUser(route,member);
      }
   }

   // 루트 이미지 생성
}
