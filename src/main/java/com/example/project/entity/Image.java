package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Image {
   @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
   private Long id;
   private String ImageUrl;

   @ManyToOne(fetch=FetchType.LAZY)
   @JoinColumn(name="access_id")
   private Access access;
}
