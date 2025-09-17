package com.example.project.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.mapper.AccessMapper;
import com.example.project.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessService {

   private final AccessMapper accessMapper;
   private final UserMapper userMapper;

   @Value("${aws.s3.bucket}")
   private String bucket;
   private final AmazonS3 amazonS3;

   // 후기 추가
   public void createAccess(AccessRequest request, String username) throws IOException {

      Access access = new Access();
      access.setTitle(request.getTitle());
      access.setContent(request.getContent());
      System.out.println(request.getContent());

      List<Image> images= new ArrayList<>();
      access.setImageUrl(images);
      access.setCreated_at(LocalDateTime.now());
      access.setWriter(userMapper.findByUsername(username));
      accessMapper.save(access);
      if(request.getImageUrl()!=null||request.getImageUrl().equals(""))
         addImage(request,access);
   }

   // 이미지 추가
   public void addImage(AccessRequest request, Access access) throws IOException {
      for(MultipartFile i : request.getImageUrl()){
         String imageName = i.getOriginalFilename();
         String saveImageName = UUID.randomUUID()+"_"+imageName;

         ObjectMetadata metadata = new ObjectMetadata();
         metadata.setContentType(i.getContentType());
         metadata.setContentLength(i.getSize());
         PutObjectRequest objectRequest = new PutObjectRequest(bucket, saveImageName, i.getInputStream(), metadata);
         amazonS3.putObject(objectRequest);


         Image image = new Image();
         image.setImageUrl(saveImageName);
         image.setAccess(access);
         accessMapper.saveImage(image,access);
      }
   }

   // 후기 수정
   public void editAccess(AccessRequest request, @Parameter(description="accessId")Long accessId, String username) throws IOException {
      Access access = accessMapper.findByAccessId(accessId);
      System.out.println(access);
      Long userId = accessMapper.findUserIdByAccessId(accessId);
      if(access==null)
         throw new NullPointerException("해당 게시글이 없습니다.");
      else if(userId!=userMapper.findByUsername(username).getId())
         throw new AccessDeniedException("해당 게시글의 작성자가 아닙니다.");
      access.setTitle(request.getTitle());
      access.setContent(request.getContent());
      access.setUpdated_at(LocalDateTime.now());
      accessMapper.editAccess(access);

      List<Long> currentImages= accessMapper.findImageByAccessId(access.getId());

      if(request.getOriginalImageUrl().size()!=0){
         for (Long image : currentImages) {
            System.out.println(image);
            if (!request.getOriginalImageUrl().contains(image)) {
               deleteImage(image);
            }
         }
      }else
         for (Long image : currentImages)
            deleteImage(image);
      if(request.getImageUrl()!=null)
         addImage(request,access);

   }

   // 이미지 삭제
   public void deleteImage(Long image){
         String imageName = accessMapper.findByImageId(image);
         DeleteObjectRequest objectRequest = new DeleteObjectRequest(bucket, imageName);
         System.out.println("url: " + imageName);
         try {
            amazonS3.deleteObject(objectRequest);
         } catch (Exception e) {
            System.out.println("이미지가 존재하지 않습니다.");
            e.printStackTrace();
         }
         accessMapper.deleteByImageId(image);
   }

   // 후기 삭제
   public void deleteAccess(Long id, String username) throws AccessDeniedException {
      Access access = accessMapper.findByAccessId(id);
      Long userId = accessMapper.findUserIdByAccessId(id);
      if(access==null)
         throw new NullPointerException("해당 게시글이 없습니다.");
      else if(userId!=userMapper.findByUsername(username).getId())
         throw new AccessDeniedException("해당 게시글의 작성자가 아닙니다.");
      for(Long imageId: accessMapper.findImageByAccessId(access.getId())) {
         deleteImage(imageId);
      }

      accessMapper.deleteImageByAccessId(access.getId());
      accessMapper.deleteAccess(access);
   }
}
