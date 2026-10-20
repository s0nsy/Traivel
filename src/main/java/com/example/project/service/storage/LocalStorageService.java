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
        String ext = safeExtension(file.getOriginalFilename());
        String key = UUID.randomUUID() + ext;
        log.debug("로컬 저장: {} → 키={}", file.getOriginalFilename(), key);
        try {
            file.transferTo(safeResolve(key));
        } catch (IOException e) {
            log.error("로컬 이미지 저장 실패: 파일={}", file.getOriginalFilename(), e);
            throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
        return key;
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(safeResolve(key));
            log.debug("로컬 이미지 삭제 완료: 키={}", key);
        } catch (IOException e) {
            log.warn("로컬 이미지 삭제 실패: 키={}, 이유={}", key, e.getMessage());
        }
    }

    @Override
    public String getPublicUrl(String key) {
        return "/access/image/" + key;
    }

    public Path resolve(String key) {
        return safeResolve(key);
    }

    private Path safeResolve(String key) {
        Path base = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path target = base.resolve(key).normalize();
        if (!target.startsWith(base)) {
            log.warn("Path traversal 시도 감지: key={}", key);
            throw new BusinessException(ErrorCode.INVALID_FILE_PATH);
        }
        return target;
    }

    private String safeExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        String ext = filename.substring(filename.lastIndexOf('.')).toLowerCase();
        return ext.matches("\\.[a-z0-9]{1,10}") ? ext : "";
    }
}
