package com.example.project.mapper;

import com.example.project.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
   User findById();
}
