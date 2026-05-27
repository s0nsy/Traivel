package com.example.project.entity;

import com.example.project.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    private String message;

    @Column(name = "type")
    private String type;

    @Column(name = "is_read")
    private boolean isRead;

    private String content;
}
