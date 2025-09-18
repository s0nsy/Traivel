package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.entity.dto.*;
import com.example.project.mapper.MemberMapper;
import com.example.project.mapper.PlaceMapper;
import com.example.project.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class RouteService {
   private final UserMapper userMapper;
   private final PlaceMapper placeMapper;
   private final MemberMapper memberMapper;

   // 루트 추가
   public Long addRoute(String username, String title){
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
      return route.getId();
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
      placeItem.setDtype(request.getDtype());

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
      if(route==null)
         throw new NullPointerException("해당 루트가 존재하지 않습니다.");
      memo.setRoute(route);
      placeMapper.saveMemo(memo);

      MemoItem memoItem = new MemoItem();
      memoItem.setMemo(memo);
      memoItem.setRoute(route);
      memoItem.setDay(memoRequest.getDay());
      memoItem.setDtype(memoRequest.getDtype());

      Map<String, Object> map = new HashMap<>();
      map.put("routeId", memoRequest.getRouteId());
      map.put("day", memoRequest.getDay());

      int maxOrder = placeMapper.findMaxOrderByRouteDay(map);
      memoItem.setOrder(maxOrder+1);
      placeMapper.saveRouteItem(memoItem);
      placeMapper.saveMemoItem(memoItem);
   }

   // 메모 수정
   public void editMemo(Long id, String content){
      Memo memo = placeMapper.findByMemoId(id);
      memo.setContent(content);
      placeMapper.editMemo(memo);
   }

   // 메모 삭제
   public String deleteMemo(Long memoId){
      Memo memo = placeMapper.findByMemoId(memoId);
      MemoItem memoItem= placeMapper.findByMemoItemId(memoId);
      if(memo==null||memoItem==null)
         return "존재하지 않는 메모입니다.";
      placeMapper.deleteMemoItem(memoItem);
      placeMapper.deleteMemo(memoId);
      placeMapper.deleteRouteItem(memoItem.getId());
      return "삭제되었습니다";
   }

   // 장소 삭제
   public String deletePlace(Long placeId) {
      Place place = placeMapper.findByPlaceId(placeId);
      PlaceItem placeItem= placeMapper.findByPlaceItemId(placeId);
      if(placeItem==null || place==null)
         return "존재하지 않는 장소입니다.";
      placeMapper.deletePlaceItem(placeItem);
      placeMapper.deletePlace(placeId);
      placeMapper.deleteRouteItem(placeItem.getId());
      return "삭제되었습니다";
   }

   // 여행 루트 전체 삭제
   @Transactional
   public void deleteRoute(Long routeId, User user) throws AccessDeniedException {
      Route route = placeMapper.findByRouteId(routeId);
      if(route==null)
         throw new NullPointerException("존재하지 않는 루트입니다.");
      Long ownerId = placeMapper.findByOwnerIdByRouteId(routeId);
      if(ownerId!=user.getId())
         throw new AccessDeniedException("해당 여행 루트 생성자가 여행 루트를 삭제할 수 있습니다.");
      placeMapper.deleteRouteUser(route,user);
      //해당 routeId 인 placeId, memoId의 리스트들
      List<Long> placeList = placeMapper.findPlaceIdByRouteId(routeId);
      List<Long> memoList = placeMapper.findMemoIdByRouteId(routeId);
      placeList.stream().forEach(placeId->deletePlace(placeId));
      memoList.stream().forEach(memoId->deleteMemo(memoId));
      placeMapper.deleteRoute(route);
   }

   // 여행 루트 입장
   public void joinRoute(String token,User user){
      Invite invite = memberMapper.findByToken(token);
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

   // 루트 블럭 변경
   public BlockDto moveBlock(Long routeId, Long blockId, int newOrder, int newDay){
      BlockDto blockDto=new BlockDto(placeMapper.findByBlockId(blockId));

      int oldOrder = blockDto.getOrder();
      blockDto.setOrder(newOrder);
      blockDto.setDay(newDay);

      List<RouteItem> routeItemList =placeMapper.findByDay(routeId, newDay);
      List<BlockDto> blocksList = routeItemList.stream().map(BlockDto::new).collect(Collectors.toList());
      Map<Integer,BlockDto> blocks = blocksList.stream().collect(Collectors.toMap(BlockDto::getOrder, Function.identity()));

      if(blockDto.getDay()==newDay) {
         for (BlockDto b : blocks.values()) {
            int order = b.getOrder();
            if (b.getId() != blockId) {
               if (oldOrder > newOrder) {
                  if (order < oldOrder && order >= newOrder) {
                     b.setOrder(order - 1);
                  }
               } else if (oldOrder < newOrder) {
                  if (order > oldOrder && order <= newOrder) {
                     b.setOrder(order + 1);
                  }
               }
            }
            placeMapper.updateRouteItem(b);
         }
      }else {
         List<RouteItem> oldRouteItemList =placeMapper.findByDay(routeId, blockDto.getDay());
         List<BlockDto> oldBlockList = oldRouteItemList.stream().map(BlockDto::new).collect(Collectors.toList());
         Map<Integer,BlockDto> oldBlocks = oldBlockList.stream().collect(Collectors.toMap(BlockDto::getOrder, Function.identity()));

         for(BlockDto b : oldBlocks.values()){
            if(b.getOrder()>oldOrder)
            b.setOrder(b.getOrder()-1);
            placeMapper.updateRouteItem(b);
         }
      }
      placeMapper.updateRouteItem(blockDto);

      return blockDto;
   }

   // 블럭 조회
   public Collection<BlockDto> getAllBlocks(Long routeId){
      List<BlockDto> list =placeMapper.findAllBlocks(routeId);
      list.sort(Comparator.comparingInt(BlockDto::getDay)
            .thenComparingInt(BlockDto::getOrder));
      return list;
   }
}
