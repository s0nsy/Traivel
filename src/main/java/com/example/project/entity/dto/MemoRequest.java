package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoRequest {

    @NotNull(message = "루트 ID를 입력해주세요.")
    private Long routeId;

    @NotBlank(message = "메모 내용을 입력해주세요.")
    private String content;

    private int order;

    @Min(value = 1, message = "일차는 1 이상이어야 합니다.")
    private int day;

    @Schema(example = "MEMO")
    private String dtype;
}
