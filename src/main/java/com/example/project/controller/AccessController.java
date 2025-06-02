package com.example.project.controller;

import com.example.project.entity.dto.AccessRequest;
import com.example.project.mapper.AccessMapper;
import com.example.project.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/access")
public class AccessController {
   private final AccessMapper accessMapper;
   private final AccessService accessService;

   // 후기 작성
   @PostMapping("/write")
   public ResponseEntity<String> createAccess(
         @RequestBody AccessRequest request,
         @AuthenticationPrincipal UserDetails userDetails){
      accessService.createAccess(request,userDetails.getUsername());
      return ResponseEntity.ok("게시글을 작성했습니다.");
   }

   // 후기 수정
   @PatchMapping("/edit")
   public ResponseEntity<String> editAccess (
         @RequestBody AccessRequest request,
         Long id,
         @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException{
      accessService.editAccess(request,id,userDetails.getUsername());
      return ResponseEntity.ok("게시글을 수정했습니다.");
   }

   // 후기 삭제
   @DeleteMapping("/delete")
   public ResponseEntity<String> deleteAccess(Long id,@AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException{
      accessService.deleteAccess(id, userDetails.getUsername());
      return ResponseEntity.ok("게시글을 삭제했습니다.");
   }
}
