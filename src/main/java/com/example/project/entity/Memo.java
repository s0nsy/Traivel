package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Data
public class Memo {
   private Long id;

   private String content;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name="route_id")
   @JsonBackReference
   private Route route;

}
