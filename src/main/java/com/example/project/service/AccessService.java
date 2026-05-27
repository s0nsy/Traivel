package com.example.project.service;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.exception.ForbiddenException;
import com.example.project.exception.NotFoundException;
import com.example.project.exception.ErrorCode;
import com.example.project.repository.AccessRepository;
import com.example.project.repository.ImageRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.storage.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccessService {

    private final AccessRepository accessRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public void createAccess(AccessRequest request, String username) {
        log.info("후기 작성 요청: 제목='{}', 작성자={}", request.getTitle(), username);
        Access access = new Access();
        access.setTitle(request.getTitle());
        access.setContent(request.getContent());
        access.setImageUrl(new ArrayList<>());
        access.setWriter(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
        accessRepository.save(access);
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty())
            addImages(request.getImageUrl(), access);
        log.info("후기 작성 완료: ID={}, 작성자={}", access.getId(), username);
    }

    public void editAccess(AccessRequest request, Long accessId, String username) {
        log.info("후기 수정 요청: ID={}, 요청자={}", accessId, username);
        Access access = accessRepository.findById(accessId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCESS_POST_NOT_FOUND));

        Long currentUserId = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)).getId();
        if (!access.getWriter().getId().equals(currentUserId)) {
            log.warn("후기 수정 권한 없음: ID={}, 요청자={}", accessId, username);
            throw new ForbiddenException(ErrorCode.NOT_ACCESS_WRITER);
        }

        access.setTitle(request.getTitle());
        access.setContent(request.getContent());
        accessRepository.save(access);

        List<Long> currentImages = imageRepository.findIdsByAccessId(access.getId());
        if (request.getOriginalImageUrl() != null && !request.getOriginalImageUrl().isEmpty()) {
            currentImages.stream()
                    .filter(id -> !request.getOriginalImageUrl().contains(id))
                    .forEach(this::deleteImage);
        } else {
            currentImages.forEach(this::deleteImage);
        }
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty())
            addImages(request.getImageUrl(), access);
        log.info("후기 수정 완료: ID={}", accessId);
    }

    public void deleteAccess(Long id, String username) {
        log.info("후기 삭제 요청: ID={}, 요청자={}", id, username);
        Access access = accessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCESS_POST_NOT_FOUND));

        Long currentUserId = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)).getId();
        if (!access.getWriter().getId().equals(currentUserId)) {
            log.warn("후기 삭제 권한 없음: ID={}, 요청자={}", id, username);
            throw new ForbiddenException(ErrorCode.NOT_ACCESS_WRITER);
        }

        imageRepository.findIdsByAccessId(access.getId()).forEach(this::deleteImage);
        accessRepository.delete(access);
        log.info("후기 삭제 완료: ID={}", id);
    }

    private void addImages(List<MultipartFile> files, Access access) {
        for (MultipartFile file : files) {
            String key = storageService.upload(file);
            Image image = new Image();
            image.setImageUrl(key);
            image.setAccess(access);
            imageRepository.save(image);
            log.debug("이미지 저장 완료: 키={}, 후기ID={}", key, access.getId());
        }
    }

    private void deleteImage(Long imageId) {
        String key = imageRepository.findUrlById(imageId);
        storageService.delete(key);
        imageRepository.deleteById(imageId);
        log.debug("이미지 삭제 완료: ID={}", imageId);
    }
}
