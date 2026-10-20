package com.example.project.entity;

import com.example.project.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "invite")
public class Invite extends BaseEntity {

    private String token;

    @Column(name = "route_id")
    private Long routeId;

    private LocalDateTime expiredAt;
}
