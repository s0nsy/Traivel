package com.example.project.mapper;

import com.example.project.entity.Invite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
   void saveInvite(Invite invite);
   Invite findByToken(String token);

}
