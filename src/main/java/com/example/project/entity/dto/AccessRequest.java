package com.example.project.entity.dto;

import com.example.project.entity.Image;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccessRequest {
   private String title;
   private String content;
   private List<ImageDto> imageUrl;
}
