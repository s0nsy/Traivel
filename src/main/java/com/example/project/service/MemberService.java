package com.example.project.service;

import com.example.project.entity.Invite;
import com.example.project.entity.Route;
import com.example.project.entity.User;
import com.example.project.kafka.NotificationEvent;
import com.example.project.kafka.NotificationProducer;
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
   private final DistributedLockService distributedLockService;
   private final NotificationProducer notificationProducer;

   // 중복 체크 → Kafka 발행 사이의 race condition 방지:
   // 분산 락으로 동일 routeId에 대한 초대 알림 요청을 직렬화해 중복 알림 전송을 막음.
   public boolean sendInviteNotification(Long routeId, Long invitedUserId, String invitedUsername, String inviterUsername, String link) {
      String lockToken = distributedLockService.acquire(routeId);
      try {
         List<String> currentMembers = inviteRepository.findMemberUsernamesByRouteId(routeId);
         if (currentMembers.contains(invitedUsername)) {
            log.info("초대 중복 차단: 대상={}는 이미 루트={}의 멤버", invitedUsername, routeId);
            return false;
         }
         notificationProducer.send(new NotificationEvent(
                 invitedUserId, invitedUsername,
                 inviterUsername + "님이 여행 일정에 초대했습니다!",
                 "INVITE", link, null   // messageId는 NotificationProducer에서 생성
         ));
         log.info("초대 알림 이벤트 발행 완료: 대상={}", invitedUsername);
         return true;
      } finally {
         distributedLockService.release(routeId, lockToken);
      }
   }

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

   public void leaveRoute(Long routeId, String username) {
      log.info("루트 퇴장 요청: 루트ID={}, 사용자={}", routeId, username);
      routeRepository.findById(routeId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 루트입니다."));
      User user = userRepository.findByUsername(username).orElseThrow();
      Long ownerId = routeRepository.findOwnerIdById(routeId);
      if (ownerId.equals(user.getId())) {
         throw new IllegalStateException("루트 소유자는 나갈 수 없습니다. 루트를 삭제해 주세요.");
      }
      List<User> members = routeRepository.findUsersByRouteId(routeId);
      if (members.stream().noneMatch(u -> u.getId().equals(user.getId()))) {
         throw new IllegalStateException("해당 루트의 멤버가 아닙니다.");
      }
      routeRepository.deleteRouteUser(routeId, user.getId());
      log.info("루트 퇴장 완료: 루트ID={}, 사용자={}", routeId, username);
   }

   public List<String> getMember(Long routeId) {
      log.debug("멤버 목록 조회: 루트ID={}", routeId);
      routeRepository.findById(routeId)
            .orElseThrow(() -> new RuntimeException("해당 루트 아이디의 루트는 존재하지 않습니다: " + routeId));
      return inviteRepository.findMemberUsernamesByRouteId(routeId);
   }
}
