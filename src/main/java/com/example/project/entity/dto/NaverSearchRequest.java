package com.example.project.entity.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class NaverSearchRequest {
   private String title;
   private String link;
   private String address;
   private String roadAddress;
   private String mapx;
   private String mapy;
   private String category;
//   private String thumbnailUrl;
}
