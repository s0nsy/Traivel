package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPlaceToRouteRequest {

    @NotNull(message = "루트 ID를 입력해주세요.")
    private Long routeId;

    @NotNull(message = "장소 정보를 입력해주세요.")
    @Valid
    private NaverPlaceDto place;

    private int order;

    @Min(value = 1, message = "일차는 1 이상이어야 합니다.")
    private int day;

    @Schema(example = "PLACE")
    private String dtype;
}
