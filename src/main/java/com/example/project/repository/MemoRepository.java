package com.example.project.repository;

import com.example.project.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m.id FROM Memo m WHERE m.route.id = :routeId")
    List<Long> findIdsByRouteId(@Param("routeId") Long routeId);
}
