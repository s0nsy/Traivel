package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessDto {
   private String title;
   private String content;
   private List<String> imageUrl;
   private String createdAt;
   private String updatedAt;
   private String writer;
   private Long routeId;
   private List<Long> selectedBlockIds;
}
