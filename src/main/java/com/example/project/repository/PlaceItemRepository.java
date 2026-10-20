package com.example.project.repository;

import com.example.project.entity.PlaceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceItemRepository extends JpaRepository<PlaceItem, Long> {

    @Query("SELECT pi FROM PlaceItem pi WHERE pi.place.id = :placeId")
    Optional<PlaceItem> findByPlaceId(@Param("placeId") Long placeId);
}
