package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemoRequest {
   private Long routeId;
   private String content;
   private int order;
   private int day;
   @Schema(example = "MEMO",accessMode=Schema.AccessMode.READ_WRITE)
   private String dType;
}
