package com.example.project.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccessRequest {
   private String title;
   private String content;
   private List<MultipartFile> imageUrl= new ArrayList<>();
   private List<Long> originalImageUrl;


}
