package com.example.project.repository;

import com.example.project.entity.Route;
import com.example.project.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Route r WHERE r.id = :id")
    Optional<Route> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT r.owner.id FROM Route r WHERE r.id = :id")
    Long findOwnerIdById(@Param("id") Long id);

    @Query("SELECT u FROM User u JOIN u.route r WHERE r.id = :routeId")
    List<User> findUsersByRouteId(@Param("routeId") Long routeId);

    @Modifying
    @Query(value = "INSERT INTO route_user(route_id, user_id) VALUES(:routeId, :userId)", nativeQuery = true)
    void addRouteUser(@Param("routeId") Long routeId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM route_user WHERE route_id = :routeId AND user_id = :userId", nativeQuery = true)
    void deleteRouteUser(@Param("routeId") Long routeId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM route_user WHERE route_id = :routeId", nativeQuery = true)
    void deleteAllRouteUsers(@Param("routeId") Long routeId);
}
