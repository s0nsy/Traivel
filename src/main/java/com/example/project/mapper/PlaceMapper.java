package com.example.project.mapper;

import com.example.project.entity.Place;
import com.example.project.entity.Route;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlaceMapper {
   Place findByPlaceTitle(String title);
   void save(@Param("route_id") Long routeId, @Param("place") Place place);

}
