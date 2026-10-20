package com.example.project.repository;

import com.example.project.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p.id FROM Place p WHERE p.route.id = :routeId")
    List<Long> findIdsByRouteId(@Param("routeId") Long routeId);
}
