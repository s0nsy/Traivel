package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Access {
   private Long id;

   private String title;
   @Column(columnDefinition="TEXT")
   private String content;

   @OneToMany(mappedBy="access", cascade=CascadeType.ALL, orphanRemoval = true)
   private List<Image> ImageUrl = new ArrayList<>();

   private LocalDateTime created_at;
   private LocalDateTime updated_at;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="user_id")
   private User writer;
}
