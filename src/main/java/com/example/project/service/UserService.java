package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
   private final UserMapper userMapper;

   public User findById(){
      return userMapper.findById();
   }
}
