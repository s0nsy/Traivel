package com.example.project.controller;

import com.example.project.service.storage.LocalStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Profile("local")
@RestController
@RequestMapping("/access/image")
@RequiredArgsConstructor
public class LocalImageController {

    private final LocalStorageService localStorageService;

    @GetMapping("/{key}")
    public ResponseEntity<Resource> serveImage(@PathVariable String key) {
        log.debug("로컬 이미지 조회: 키={}", key);
        Resource resource = new FileSystemResource(localStorageService.resolve(key));
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(resolveMediaType(key));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private MediaType resolveMediaType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (lower.endsWith(".png"))                            return MediaType.IMAGE_PNG;
        if (lower.endsWith(".gif"))                            return MediaType.IMAGE_GIF;
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
