package com.example.project.entity;

import com.example.project.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "users")
public class User extends BaseEntity {

    private String username;

    private String password;

    private String email;

    private String role;

    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private String uuid;

    @ManyToMany(mappedBy = "users")
    private List<Route> route = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Access> accesses = new ArrayList<>();

    @PrePersist
    private void generateUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }
}
