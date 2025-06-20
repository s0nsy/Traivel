package com.example.project.entity.dto;

import lombok.Data;
@Data
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
