package com.example.project.service;

import com.example.project.entity.Invite;
import com.example.project.entity.Route;
import com.example.project.entity.User;
import com.example.project.mapper.MemberMapper;
import com.example.project.mapper.PlaceMapper;
import com.example.project.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
   private final PlaceMapper placeMapper;
   private final UserMapper userMapper;
   private final MemberMapper memberMapper;

   // 초대 링크 생성
   public String createInviteLink(Long routeId, String username) throws AccessDeniedException {
      Route route = placeMapper.findByRouteId(routeId);
      if(route==null)
         throw new NullPointerException("존재하지 않는 루트입니다.");
      Long ownerId = placeMapper.findByOwnerIdByRouteId(route.getId());
      User user = userMapper.findByUsername(username);
      if(ownerId!=user.getId())
         throw new AccessDeniedException("해당 여행 루트 생성자가 여행 루트를 삭제할 수 있습니다.");
      String token = UUID.randomUUID().toString();
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime expiredAt= now.plusDays(1);

      Invite invite = new Invite();
      invite.setRoute_id(routeId);
      invite.setToken(token);
      invite.setExpired_at(expiredAt);
      memberMapper.saveInvite(invite);

      return "https://traivel.p-e.kr.com/invite/" +token;
   }

   // 멤버 방출
   public void deleteMember(Long routeId, String username, User member) throws AccessDeniedException {
      Route route = placeMapper.findByRouteId(routeId);
      if(route==null)
         throw new NullPointerException("존재하지 않는 루트입니다.");
      Long ownerId = placeMapper.findByOwnerIdByRouteId(route.getId());
      User user = userMapper.findByUsername(username);
      if(ownerId!=user.getId())
         throw new AccessDeniedException("루트 관리자가 아닙니다. 멤버를 방출시키실 수 없습니다.");
      List<User> users= placeMapper.findUsersByRouteId(routeId);
      if(users.contains(member)) {
         users.remove(member);
         placeMapper.deleteRouteUser(route,member);
      }
   }
}
