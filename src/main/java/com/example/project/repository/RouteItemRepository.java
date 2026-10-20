package com.example.project.repository;

import com.example.project.entity.RouteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteItemRepository extends JpaRepository<RouteItem, Long> {

    @Query("SELECT COALESCE(MAX(ri.order), 0) FROM RouteItem ri WHERE ri.route.id = :routeId AND ri.day = :day")
    int findMaxOrderByRouteIdAndDay(@Param("routeId") Long routeId, @Param("day") int day);

    @Query("SELECT ri FROM RouteItem ri WHERE ri.route.id = :routeId AND ri.day = :day ORDER BY ri.order")
    List<RouteItem> findByRouteIdAndDay(@Param("routeId") Long routeId, @Param("day") int day);

    @Query("SELECT ri FROM RouteItem ri WHERE ri.route.id = :routeId ORDER BY ri.day, ri.order")
    List<RouteItem> findByRouteId(@Param("routeId") Long routeId);

    @Modifying
    @Query("UPDATE RouteItem ri SET ri.day = :day, ri.order = :order WHERE ri.id = :id")
    void updateOrderAndDay(@Param("id") Long id, @Param("order") int order, @Param("day") int day);

    // 같은 day 내 아래로 이동
    @Modifying
    @Query("UPDATE RouteItem ri SET ri.order = ri.order - 1 " +
           "WHERE ri.route.id = :routeId AND ri.day = :day " +
           "AND ri.order > :fromEx AND ri.order <= :toIn AND ri.id <> :excludeId")
    void shiftDownBetween(@Param("routeId") Long routeId, @Param("day") int day,
                          @Param("fromEx") int fromEx, @Param("toIn") int toIn,
                          @Param("excludeId") Long excludeId);

    // 같은 day 내 위로 이동
    @Modifying
    @Query("UPDATE RouteItem ri SET ri.order = ri.order + 1 " +
           "WHERE ri.route.id = :routeId AND ri.day = :day " +
           "AND ri.order >= :fromIn AND ri.order < :toEx AND ri.id <> :excludeId")
    void shiftUpBetween(@Param("routeId") Long routeId, @Param("day") int day,
                        @Param("fromIn") int fromIn, @Param("toEx") int toEx,
                        @Param("excludeId") Long excludeId);

    // 다른 day로 이동 — 구 day: 빈 자리로 이동
    @Modifying
    @Query("UPDATE RouteItem ri SET ri.order = ri.order - 1 " +
           "WHERE ri.route.id = :routeId AND ri.day = :day " +
           "AND ri.order > :afterOrder AND ri.id <> :excludeId")
    void shiftDownFrom(@Param("routeId") Long routeId, @Param("day") int day,
                       @Param("afterOrder") int afterOrder, @Param("excludeId") Long excludeId);

    // 다른 day로 이동 — 새 day: 삽입 공간 확보
    @Modifying
    @Query("UPDATE RouteItem ri SET ri.order = ri.order + 1 " +
           "WHERE ri.route.id = :routeId AND ri.day = :day " +
           "AND ri.order >= :fromOrder AND ri.id <> :excludeId")
    void shiftUpFrom(@Param("routeId") Long routeId, @Param("day") int day,
                     @Param("fromOrder") int fromOrder, @Param("excludeId") Long excludeId);
}
