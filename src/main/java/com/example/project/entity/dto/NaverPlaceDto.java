package com.example.project.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverPlaceDto {

    @NotBlank(message = "장소명을 입력해주세요.")
    private String title;

    private String link;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    private String roadAddress;
    private String telephone;
    private String mapx;
    private String mapy;
    private String category;
    private String imageUrl;
}
