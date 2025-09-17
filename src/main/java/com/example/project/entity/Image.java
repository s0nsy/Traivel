package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
public class Image {
   private Long id;
   private String ImageUrl;

   @ManyToOne(fetch=FetchType.LAZY)
   @JoinColumn(name="access_id")
   private Access access;
}
