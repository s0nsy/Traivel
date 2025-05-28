package com.example.project.mapper;

import com.example.project.entity.*;
import com.example.project.entity.dto.BlockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlaceMapper {
   Route findByRouteId(Long id);
   void savePlace(Place place);
   void addRoute(Route route);
   void addRouteUser(@Param("route") Route route, @Param("users") User user);
   void deleteRouteUser(@Param("route") Route route, @Param("users") User user);
   int findMaxOrderByRouteDay(Map map);
   void saveRouteItem(RouteItem routeItem);
   void savePlaceItem(PlaceItem placeItem);
   void saveMemo(Memo memo);
   void editMemo(Memo memo);
   void saveMemoItem(MemoItem memoItem);
   void deleteRouteItem(Long id);
   void deleteMemoItem(MemoItem memoItem);
   void deletePlaceItem(PlaceItem placeItem);
   Memo findByMemoId(Long id);
   MemoItem findByMemoItemId(Long id);
   PlaceItem findByPlaceItemId(Long id);
   void deleteRoute(Route route);
   void saveInvite(Invite invite);
   Invite findByToken(String token);
   Long findByOwnerIdByRouteId(Long id);
   List<User> findUsersByRouteId(Long id);

   RouteItem findByBlockId(Long id);
   List<RouteItem> findByDay(Long routeId, int day);
   void updateRouteItem(BlockDto item);
   List<BlockDto> findAllBlocks(Long routeId);
}
