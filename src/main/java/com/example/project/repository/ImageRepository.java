package com.example.project.repository;

import com.example.project.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.id FROM Image i WHERE i.access.id = :accessId")
    List<Long> findIdsByAccessId(@Param("accessId") Long accessId);

    @Query("SELECT i.ImageUrl FROM Image i WHERE i.id = :id")
    String findUrlById(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.access.id = :accessId")
    void deleteByAccessId(@Param("accessId") Long accessId);
}
