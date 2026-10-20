package com.example.project.service;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.document.AccessDocument;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.entity.dto.AccessSummaryDto;
import com.example.project.exception.BusinessException;
import com.example.project.exception.ForbiddenException;
import com.example.project.exception.NotFoundException;
import com.example.project.exception.ErrorCode;
import com.example.project.repository.AccessRepository;
import com.example.project.repository.AccessSearchRepository;
import com.example.project.repository.ImageRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.storage.StorageService;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccessService {

    private final AccessRepository accessRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final AccessSearchRepository accessSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    public void createAccess(AccessRequest request, String username, Long routeId) {
        log.info("후기 작성 요청: 제목='{}', 작성자={}", request.getTitle(), username);
        Access access = new Access();
        access.setTitle(request.getTitle());
        access.setContent(request.getContent());
        access.setImageUrl(new ArrayList<>());
        access.setWriter(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
        access.setRouteId(routeId);
        access.setSelectedBlockIds(request.getSelectedBlockIds() != null ? request.getSelectedBlockIds() : new ArrayList<>());
        accessRepository.save(access);
        accessSearchRepository.save(AccessDocument.from(access));
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
        List<String> uploadedKeys = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String contentType = file.getContentType();
                if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
                    log.warn("허용되지 않는 파일 형식 업로드 시도: contentType={}, 후기ID={}", contentType, access.getId());
                    throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
                }
                String key = storageService.upload(file);
                uploadedKeys.add(key);
                Image image = new Image();
                image.setImageUrl(key);
                image.setAccess(access);
                imageRepository.save(image);
                log.debug("이미지 저장 완료: 키={}, 후기ID={}", key, access.getId());
            }
        } catch (Exception e) {
            uploadedKeys.forEach(key -> {
                try {
                    storageService.delete(key);
                    log.warn("업로드 실패 보상 — S3 파일 정리: key={}", key);
                } catch (Exception ex) {
                    log.error("S3 보상 삭제 실패 (고아 파일 발생): key={}", key, ex);
                }
            });
            throw e;
        }
    }

    private void deleteImage(Long imageId) {
        String key = imageRepository.findUrlById(imageId);
        imageRepository.deleteById(imageId);
        try {
            storageService.delete(key);
            log.debug("이미지 삭제 완료: ID={}", imageId);
        } catch (Exception e) {
            log.error("스토리지 파일 삭제 실패 (DB는 삭제됨, 고아 파일 발생): key={}", key, e);
        }
    }

    public List<AccessSummaryDto> searchAccess(String key){
        NativeQuery query = NativeQuery.builder()
            .withQuery(q->q.multiMatch(m-> m.query(key)
                .fields("title","content"))).build();
        return elasticsearchOperations.search(query, AccessDocument.class)
            .stream().map(hit->{
                AccessDocument doc = hit.getContent();
                return AccessSummaryDto.builder().id(doc.getId()).title(doc.getTitle()).writer(doc.getWriter())
                    .build();
            })
            .collect(Collectors.toList());
    }
}
