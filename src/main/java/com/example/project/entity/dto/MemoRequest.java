package com.example.project.entity.dto;

import lombok.Data;

@Data
public class MemoRequest {
   private Long routeId;
   private String content;
   private int order;
   private int day;
   private String dType;
}
