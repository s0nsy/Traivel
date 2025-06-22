package com.example.project.controller;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.dto.AccessDto;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.entity.dto.AccessSummaryDto;
import com.example.project.mapper.AccessMapper;
import com.example.project.mapper.UserMapper;
import com.example.project.service.AccessService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;



@RequiredArgsConstructor
@RestController
@RequestMapping("/access")
public class AccessController {
   private final AccessService accessService;
   private final AccessMapper accessMapper;
   private final UserMapper userMapper;

   // 후기 목록
   @GetMapping
   public List<AccessSummaryDto> showAccess(){
      List<AccessSummaryDto> dtos = new ArrayList<>();
      for(Access access: accessMapper.findSummaryAll()){
         AccessSummaryDto dto = new AccessSummaryDto();
         dto.setTitle(access.getTitle());
         dto.setWriter(access.getWriter().getUsername());
         dto.setCreatedAt(access.getCreated_at());
         dtos.add(dto);
      }
      return dtos;
   }


   // 후기 작성
   @PostMapping("/write")
   @Operation(description = "postman에서 진행해주세요.")
   public ResponseEntity<String> createAccess(
         @ModelAttribute AccessRequest request,
         @AuthenticationPrincipal UserDetails userDetails) throws IOException {
      accessService.createAccess(request,userDetails.getUsername());
      return ResponseEntity.ok("게시글을 작성했습니다.");
   }

   // 후기 상세 페이지
   @GetMapping("/{accessId}")
   public AccessDto AccessDetailed(@PathVariable Long accessId){
//      AccessDto accessDto = accessToAccessDto.fromEntity(accessMapper.findByAccessId(accessId));
      AccessDto accessDto = new AccessDto();
      Access access = accessMapper.findByAccessId(accessId);
      accessDto.setTitle(access.getTitle());
      accessDto.setContent(access.getContent());
      accessDto.setWriter(userMapper.findUsernameById(accessMapper.findUserIdByAccessId(accessId)));
      accessDto.setImageUrl(accessMapper.findImageByAccessId(accessId));
      accessDto.setCreatedAt(String.valueOf(access.getCreated_at()));
      return accessDto;
   }

   // 후기 수정
   @PatchMapping("/{accessId}")
   @Operation(description = "postman에서 진행해주세요.")
   public ResponseEntity<String> editAccess (
         @PathVariable Long accessId,
         @ModelAttribute AccessRequest request,
         @AuthenticationPrincipal UserDetails userDetails) throws IOException{
      accessService.editAccess(request,accessId,userDetails.getUsername());
      return ResponseEntity.ok("게시글을 수정했습니다.");
   }

   // 후기 삭제
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteAccess(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException{
      accessService.deleteAccess(id, userDetails.getUsername());
      return ResponseEntity.ok("게시글을 삭제했습니다.");
   }

   @GetMapping("/image/{image}")
   public ResponseEntity<Resource> image(@PathVariable String image){
      String basePath;
      if(System.getProperty("os.name").toLowerCase().contains("win"))
         basePath = "C:/Users/82104/IdeaProjects/Project/images/";
      else
         basePath = "/home/ubuntu/images";
      File file = new File(basePath+image);
      FileSystemResource resource = new FileSystemResource(file);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(getMediaType(image));

      return new ResponseEntity<>(resource, headers, HttpStatus.OK);
   }

   private MediaType getMediaType(String image){
      String lower = image.toLowerCase();
      if(lower.endsWith(".jpg")||lower.endsWith(".jpeg"))
         return MediaType.IMAGE_JPEG;
      else if(lower.endsWith(".png"))
         return MediaType.IMAGE_PNG;
      else if(lower.endsWith(".gif"))
         return MediaType.IMAGE_GIF;
      return MediaType.APPLICATION_OCTET_STREAM;
   }


}
