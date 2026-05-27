package com.example.project.repository;

import com.example.project.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findByToken(String token);

    @Query("SELECT u.username FROM User u JOIN u.route r WHERE r.id = :routeId")
    List<String> findMemberUsernamesByRouteId(@Param("routeId") Long routeId);
}
