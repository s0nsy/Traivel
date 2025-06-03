package com.example.project.mapper;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccessMapper {
   void save(Access access);
   void saveImage(@Param("image")Image image, @Param("access")Access access);
   Access findByAccessId(Long id);
   void editAccess(Access access);
   void deleteAccess(Access access);
   Long findUserIdByAccessId(Long id);
   void deleteImageByAccessId(Long id);
}
