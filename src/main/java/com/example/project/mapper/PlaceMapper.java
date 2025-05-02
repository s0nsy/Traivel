package com.example.project.mapper;

import com.example.project.entity.Place;
import com.example.project.entity.PlaceItem;
import com.example.project.entity.Route;
import com.example.project.entity.RouteItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface PlaceMapper {
   Route findByRouteId(Long id);
   void save(Place place);
   int findMaxOrderByRouteDay(Map map);
   void saveRouteItem(PlaceItem placeItem);
   void savePlaceItem(PlaceItem placeItem);
}
