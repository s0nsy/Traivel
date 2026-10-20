package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendRouteRequest {

   @Valid
   @NotNull(message = "여행 정보를 입력해주세요.")
   private TravelRequest travelRequest;

   @NotBlank(message = "목적지를 입력해주세요.")
   @Schema(example="전라남도 순천")
   private String destination;

   @NotNull(message = "특징을 입력해주세요.")
   @Size(min = 3, max = 3, message = "특징은 정확히 3개여야 합니다.")
   private List<String> features;
}
