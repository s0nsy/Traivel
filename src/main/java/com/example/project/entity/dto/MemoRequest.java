package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoRequest {
   private Long routeId;
   private String content;
   private int order;
   private int day;
   @Schema(example = "MEMO")
   @Column(insertable = false, updatable = false)
   private String dtype;
   // Springdoc 2.x에서 지원하는 필드 기반 접근
}
