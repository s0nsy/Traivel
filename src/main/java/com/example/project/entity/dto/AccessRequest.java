package com.example.project.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccessRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    private String title;

    private String content;

    private List<MultipartFile> imageUrl = new ArrayList<>();

    private List<Long> originalImageUrl;

    private List<Long> selectedBlockIds = new ArrayList<>();

}
