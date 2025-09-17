package com.example.project.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AccessSummaryDto {
   Long id;
   String title;
   LocalDateTime createdAt;
   String writer;
}
