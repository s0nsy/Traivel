package com.example.project.service;

import com.example.project.entity.Invite;
import com.example.project.entity.Route;
import com.example.project.entity.User;
import com.example.project.repository.InviteRepository;
import com.example.project.repository.RouteRepository;
import com.example.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
   private final RouteRepository routeRepository;
   private final UserRepository userRepository;
   private final InviteRepository inviteRepository;

   public String createInviteLink(Long routeId, String username) throws AccessDeniedException {
      log.info("초대 링크 생성 요청: 루트ID={}, 요청자={}", routeId, username);
      Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 루트입니다."));
      Long ownerId = routeRepository.findOwnerIdById(route.getId());
      User user = userRepository.findByUsername(username).orElseThrow();
      if (!ownerId.equals(user.getId())) {
         log.warn("초대 링크 생성 권한 없음: 루트ID={}, 요청자={}", routeId, username);
         throw new AccessDeniedException("해당 여행 루트 생성자가 여행 루트를 삭제할 수 있습니다.");
      }

      String token = UUID.randomUUID().toString();
      Invite invite = new Invite();
      invite.setRouteId(routeId);
      invite.setToken(token);
      invite.setExpiredAt(LocalDateTime.now().plusDays(1));
      inviteRepository.save(invite);
      log.info("초대 링크 생성 완료: 루트ID={}, 만료={}", routeId, invite.getExpiredAt());
      return token;
   }

   public void deleteMember(Long routeId, String username, User member) throws AccessDeniedException {
      log.info("멤버 방출 요청: 루트ID={}, 요청자={}, 대상={}", routeId, username, member.getUsername());
      routeRepository.findById(routeId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 루트입니다."));
      Long ownerId = routeRepository.findOwnerIdById(routeId);
      User user = userRepository.findByUsername(username).orElseThrow();
      if (!ownerId.equals(user.getId())) {
         log.warn("멤버 방출 권한 없음: 루트ID={}, 요청자={}", routeId, username);
         throw new AccessDeniedException("루트 관리자가 아닙니다. 멤버를 방출시키실 수 없습니다.");
      }

      List<User> users = routeRepository.findUsersByRouteId(routeId);
      if (users.contains(member)) {
         routeRepository.deleteRouteUser(routeId, member.getId());
         log.info("멤버 방출 완료: 루트ID={}, 대상={}", routeId, member.getUsername());
      } else {
         log.warn("멤버 방출 실패 - 루트에 없는 멤버: 루트ID={}, 대상={}", routeId, member.getUsername());
      }
   }

   public List<String> getMember(Long routeId) {
      log.debug("멤버 목록 조회: 루트ID={}", routeId);
      routeRepository.findById(routeId)
            .orElseThrow(() -> new RuntimeException("해당 루트 아이디의 루트는 존재하지 않습니다: " + routeId));
      return inviteRepository.findMemberUsernamesByRouteId(routeId);
   }
}
