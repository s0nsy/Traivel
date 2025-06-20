package com.example.project.service;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.mapper.AccessMapper;
import com.example.project.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
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
   private final String uploadDir ="C:/Users/82104/IdeaProjects/Project/images/";

   public void createAccess(AccessRequest request, String username) throws IOException {
//      String uploadDir= "/home/ubuntu/traivel/image";


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
         for(MultipartFile i : request.getImageUrl()){
            String imageName = i.getOriginalFilename();
            String saveImageName = UUID.randomUUID()+"_"+imageName;

            File dest = new File(uploadDir+saveImageName);
            i.transferTo(dest);

            Image image = new Image();
            image.setImageUrl(saveImageName);
            image.setAccess(access);
            images.add(image);
            accessMapper.saveImage(image,access);
         }

   }

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
//      String uploadDir= "/home/ubuntu/traivel/image";

      if(request.getOriginalImageUrl()!=null)
         for (Long image : currentImages) {
            if (!request.getOriginalImageUrl().contains(image)) {
               File dest = new File(uploadDir + accessMapper.findByImageId(image));
               if (dest.exists())
                  dest.delete();
               accessMapper.deleteByImageId(image);
            }
         }
      if(request.getImageUrl()!=null && !request.getImageUrl().equals(""))
         for (MultipartFile image : request.getImageUrl()) {
            String name = image.getOriginalFilename();
            String saveImageName = UUID.randomUUID() + "_" + name;

            File dest = new File(uploadDir + saveImageName);
            image.transferTo(dest);

            Image newImage = new Image();
            newImage.setImageUrl(saveImageName);
            newImage.setAccess(access);
            accessMapper.saveImage(newImage, access);

         }

   }

   public void deleteAccess(Long id, String username) throws AccessDeniedException {
      Access access = accessMapper.findByAccessId(id);
      Long userId = accessMapper.findUserIdByAccessId(id);
      if(access==null)
         throw new NullPointerException("해당 게시글이 없습니다.");
      else if(userId!=userMapper.findByUsername(username).getId())
         throw new AccessDeniedException("해당 게시글의 작성자가 아닙니다.");
      for(Long imageId: accessMapper.findImageByAccessId(access.getId())) {
         File dest = new File(uploadDir + accessMapper.findByImageId(imageId));
         System.out.println(dest);
         if (dest.exists())
            dest.delete();
      }

      accessMapper.deleteImageByAccessId(access.getId());
      accessMapper.deleteAccess(access);
   }
}
