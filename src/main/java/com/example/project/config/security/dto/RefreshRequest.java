package com.example.project.config.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshRequest {

    @NotBlank(message = "refresh token을 입력해주세요.")
    private String refreshToken;
}
