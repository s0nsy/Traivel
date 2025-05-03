package com.example.project.mapper;

import com.example.project.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PlaceMapper {
   void saveRoute(Route route);
   Route findByRouteId(Long id);
   void savePlace(Place place);
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
}
