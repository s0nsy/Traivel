package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessSummaryDto {
   Long id;
   String title;
   LocalDateTime createdAt;
   String writer;
}
