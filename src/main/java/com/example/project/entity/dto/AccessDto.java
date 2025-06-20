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
   private List<Long> imageUrl;
   private String createdAt;
   private String updatedAt;
   private String writer;

}
