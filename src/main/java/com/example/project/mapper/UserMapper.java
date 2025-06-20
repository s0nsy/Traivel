package com.example.project.mapper;

import com.example.project.entity.Notification;
import com.example.project.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
   User findByUsername(String username);
   void save(User user);
   List<Notification> findNotificationsByUserId(Long id);
   void addNotification(Notification notification);
   String findUsernameById(Long id);
}
