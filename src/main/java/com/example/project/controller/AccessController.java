package com.example.project.controller;

import com.example.project.entity.Access;
import com.example.project.entity.dto.AccessDto;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.entity.dto.AccessSummaryDto;
import jakarta.validation.Valid;
import com.example.project.repository.AccessRepository;
import com.example.project.repository.ImageRepository;
import com.example.project.service.AccessService;
import com.example.project.service.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/access")
public class AccessController {
   private final AccessService accessService;
   private final AccessRepository accessRepository;
   private final ImageRepository imageRepository;
   private final StorageService storageService;

   @GetMapping
   public List<AccessSummaryDto> showAccess() {
      log.debug("후기 목록 조회 요청");
      List<AccessSummaryDto> dtos = new ArrayList<>();
      for (Access access : accessRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))) {
         AccessSummaryDto dto = new AccessSummaryDto();
         dto.setId(access.getId());
         dto.setTitle(access.getTitle());
         dto.setWriter(access.getWriter().getUsername());
         dto.setCreatedAt(access.getCreatedAt());
         dtos.add(dto);
      }
      log.debug("후기 목록 조회 완료: 총 {}건", dtos.size());
      return dtos;
   }

   @PostMapping("/write")
   @Operation(description = "postman에서 진행해주세요.")
   public ResponseEntity<String> createAccess(
         @Valid @ModelAttribute AccessRequest request,
         @AuthenticationPrincipal UserDetails userDetails,
       @RequestParam(required = false) Long routeId) throws IOException {
      log.info("후기 작성 요청: 작성자={}", userDetails.getUsername());
      accessService.createAccess(request, userDetails.getUsername(), routeId);
      return ResponseEntity.ok("게시글을 작성했습니다.");
   }

   @GetMapping("/{accessId}")
   public AccessDto AccessDetailed(@PathVariable Long accessId) {
      log.debug("후기 상세 조회: ID={}", accessId);
      AccessDto accessDto = new AccessDto();
      Access access = accessRepository.findById(accessId)
            .orElseThrow(() -> new RuntimeException("해당 게시글이 없습니다."));
      accessDto.setTitle(access.getTitle());
      accessDto.setContent(access.getContent());
      accessDto.setWriter(access.getWriter().getUsername());
      List<String> imageUrls = imageRepository.findKeysByAccessId(accessId).stream()
            .map(storageService::getPublicUrl)
            .collect(Collectors.toList());
      accessDto.setImageUrl(imageUrls);
      accessDto.setCreatedAt(String.valueOf(access.getCreatedAt()));
      accessDto.setRouteId(access.getRouteId());
      accessDto.setSelectedBlockIds(access.getSelectedBlockIds());
      return accessDto;
   }

   @PatchMapping("/{accessId}")
   @Operation(description = "postman에서 진행해주세요.\nimageUrl은 새로운 파일, originalImageUrl은 후기에 업로드 되어있는 파일입니다.\n 이미지를 imageUrl에서 추가하거나 originalImageUrl에서 삭제할 수 있습니다.")
   public ResponseEntity<String> editAccess(
         @PathVariable Long accessId,
         @Valid @ModelAttribute AccessRequest request,
         @AuthenticationPrincipal UserDetails userDetails) throws IOException {
      log.info("후기 수정 요청: ID={}, 요청자={}", accessId, userDetails.getUsername());
      accessService.editAccess(request, accessId, userDetails.getUsername());
      return ResponseEntity.ok("게시글을 수정했습니다.");
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteAccess(
         @PathVariable Long id,
         @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
      log.info("후기 삭제 요청: ID={}, 요청자={}", id, userDetails.getUsername());
      accessService.deleteAccess(id, userDetails.getUsername());
      return ResponseEntity.ok("게시글을 삭제했습니다.");
   }

   @GetMapping("/search")
   public List<AccessSummaryDto> searchAccess(@RequestParam String q){
      return accessService.searchAccess(q);
   }
}
