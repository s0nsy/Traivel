package com.example.project.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class AccessRequest {
   private String title;
   private String content;
   private List<ImageDto> imageUrl;
}
