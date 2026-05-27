package com.example.project.service.storage;

import com.example.project.exception.BusinessException;
import com.example.project.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@Profile("local")
public class LocalStorageService implements StorageService {

    @Value("${storage.local.path}")
    private String uploadPath;

    @PostConstruct
    private void init() throws IOException {
        Files.createDirectories(Paths.get(uploadPath));
        log.info("로컬 이미지 저장 경로: {}", Paths.get(uploadPath).toAbsolutePath());
    }

    @Override
    public String upload(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.debug("로컬 저장: {} → 키={}", file.getOriginalFilename(), key);
        try {
            file.transferTo(Paths.get(uploadPath).resolve(key));
        } catch (IOException e) {
            log.error("로컬 이미지 저장 실패: 파일={}", file.getOriginalFilename(), e);
            throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
        return key;
    }

    @Override
    public void delete(String key) {
        try {
            Path target = Paths.get(uploadPath).resolve(key);
            Files.deleteIfExists(target);
            log.debug("로컬 이미지 삭제 완료: 키={}", key);
        } catch (IOException e) {
            log.warn("로컬 이미지 삭제 실패: 키={}, 이유={}", key, e.getMessage());
        }
    }

    public Path resolve(String key) {
        return Paths.get(uploadPath).resolve(key);
    }
}
