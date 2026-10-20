package com.example.project.controller;

import com.example.project.entity.Invite;
import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.entity.dto.NotificationResponse;
import com.example.project.repository.InviteRepository;
import com.example.project.repository.NotificationRepository;
import com.example.project.service.MemberService;
import com.example.project.service.RouteService;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
   private final RouteService routeService;
   private final UserService userService;
   private final MemberService memberService;
   private final NotificationRepository notificationRepository;
   private final InviteRepository inviteRepository;

   @PostMapping("/link")
   public ResponseEntity<String> createLink(Long routeId, Principal principal) throws AccessDeniedException {
      log.info("초대 링크 생성 요청: 루트ID={}, 요청자={}", routeId, principal.getName());
      String linkToken = memberService.createInviteLink(routeId, principal.getName());
      return ResponseEntity.ok(linkToken);
   }

   @PostMapping("/invite/notification")
   public ResponseEntity<String> invite(
         String username, String link,
         @AuthenticationPrincipal UserDetails userDetails) throws AuthenticationException, AccessDeniedException {
      log.info("초대 알림 전송 요청: 대상={}, 요청자={}", username, userDetails.getUsername());
      User user = userService.findByUsername(userDetails.getUsername());
      if (user == null) throw new AccessDeniedException("해당 루트 관리자가 아닙니다.");

      User invitedUser = userService.findByUsername(username);
      if (invitedUser == null) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

      Invite invite = inviteRepository.findByToken(link)
              .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 링크입니다."));

      boolean sent = memberService.sendInviteNotification(
              invite.getRouteId(), invitedUser.getId(), username, userDetails.getUsername(), link);
      if (!sent) {
         return ResponseEntity.badRequest().body("이미 해당 일정의 멤버로 속해있습니다.");
      }
      return ResponseEntity.ok("알림 추가했습니다.");
   }

   @Operation(description = "초대 코드의 UUID를 작성해주세요.")
   @GetMapping("/invite/{token}")
   public ResponseEntity<String> joinRoute(
         @PathVariable String token,
         @AuthenticationPrincipal UserDetails userDetails) {
      log.info("루트 입장 요청: 토큰={}, 사용자={}", token, userDetails.getUsername());
      routeService.joinRoute(token, userDetails.getUsername());
      return ResponseEntity.ok("정상적으로 초대되었습니다.");
   }

   @DeleteMapping("/leave")
   public ResponseEntity<String> leaveRoute(
         Long routeId,
         @AuthenticationPrincipal UserDetails userDetails) {
      log.info("루트 퇴장 요청: 루트ID={}, 사용자={}", routeId, userDetails.getUsername());
      memberService.leaveRoute(routeId, userDetails.getUsername());
      return ResponseEntity.ok("루트에서 나갔습니다.");
   }

   @DeleteMapping("/eviction/member")
   public ResponseEntity<String> deleteMember(
         Long routeId,
         @AuthenticationPrincipal UserDetails userDetails,
         String username) throws AccessDeniedException {
      log.info("멤버 방출 요청: 루트ID={}, 대상={}, 요청자={}", routeId, username, userDetails.getUsername());
      User member = userService.findByUsername(username);
      memberService.deleteMember(routeId, userDetails.getUsername(), member);
      return ResponseEntity.ok(username + "을 퇴장시켰습니다.");
   }

   @GetMapping("/notification")
   public List<NotificationResponse> getNotification(@AuthenticationPrincipal UserDetails userDetails) {
      log.debug("알림 조회 요청: {}", userDetails.getUsername());
      User user = userService.findByUsername(userDetails.getUsername());
      if (user == null) throw new RuntimeException("오류가 발생했습니다.");
      List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
      return list.stream().map(NotificationResponse::new).toList();
   }

   @GetMapping("/list/{routeId}")
   public List<String> getMember(@PathVariable Long routeId, Principal principal) {
      log.debug("멤버 목록 조회: 루트ID={}", routeId);
      User user = userService.findByUsername(principal.getName());
      if (user == null) throw new RuntimeException("오류가 발생했습니다.");
      routeService.assertMember(routeId, principal.getName());
      return memberService.getMember(routeId);
   }
}
