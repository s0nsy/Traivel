package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
public class Memo {
   private Long id;

   private String content;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="route_id")
   private Route route;

}
