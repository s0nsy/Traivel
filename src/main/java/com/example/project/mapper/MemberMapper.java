package com.example.project.mapper;

import com.example.project.entity.Invite;
import com.example.project.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
   void saveInvite(Invite invite);
   Invite findByToken(String token);
   List<Notification> findNotificationByUserId(Long id);
}
