package com.example.project.entity.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemoRequest {
   private Long routeId;
   private String content;
   private int order;
   private int day;

}
