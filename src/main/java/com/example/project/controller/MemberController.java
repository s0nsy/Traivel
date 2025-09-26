package com.example.project.controller;

import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.entity.dto.NotificationDto;
import com.example.project.mapper.MemberMapper;
import com.example.project.mapper.UserMapper;
import com.example.project.service.MemberService;
import com.example.project.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
   private final RouteService routeService;
   private final UserMapper userMapper;
   private final MemberService memberService;
   private final MemberMapper memberMapper;

   // 초대 링크 생성
   @PostMapping("/link")
   public ResponseEntity<String> createLink(Long routeId, Principal principal) throws AccessDeniedException {
      String linkToken =memberService.createInviteLink(routeId, principal.getName());
      return ResponseEntity.ok(linkToken);
   }

   // 초대 알림 전송
   @PostMapping("/invite/notification")
   public ResponseEntity<String> invite(String username, String link, @AuthenticationPrincipal UserDetails userDetails) throws AuthenticationException, AccessDeniedException {
      User user = userMapper.findByUsername(userDetails.getUsername());
      if(user==null)
         throw new AccessDeniedException("해당 루트 관리자가 아닙니다.");
      Notification newNotification = new Notification();
      User invitedUser = userMapper.findByUsername(username);
      if(invitedUser==null)
         throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
      newNotification.setUserId(invitedUser.getId());
      String message = userDetails.getUsername()+"님이 여행 일정에 초대했습니다!";
      newNotification.setMessage(message);
      newNotification.setType("INVITE");
      newNotification.setCreatedAt(LocalDateTime.now());
      newNotification.setContent(link);
      userMapper.addNotification(newNotification);
      return ResponseEntity.ok("알림 추가했습니다.");
   }

   // 링크를 통해 여행 루트 입장
   @Operation(description="초대 코드의 UUID를 작성해주세요.")
   @GetMapping("/invite/{token}")
   public ResponseEntity<String> joinRoute(@PathVariable String token, @AuthenticationPrincipal UserDetails userDetails){
      User user = userMapper.findByUsername(userDetails.getUsername());
      routeService.joinRoute(token,user);
      return ResponseEntity.ok("정상적으로 초대되었습니다.");
   }

   // 멤버 방출
   @DeleteMapping("/eviction/member")
   public ResponseEntity<String> deleteMember(Long routeId, @AuthenticationPrincipal UserDetails userDetails, String username) throws AccessDeniedException {
      User member = userMapper.findByUsername(username);
      memberService.deleteMember(routeId,userDetails.getUsername(),member);
      return ResponseEntity.ok(username+ "을 퇴장시켰습니다.");
   }

   // 알림 조회
   @GetMapping("/notification")
   public List<NotificationDto> getNotification(@AuthenticationPrincipal UserDetails userDetails){
      User user = userMapper.findByUsername(userDetails.getUsername());
      if(user==null)
         throw new RuntimeException("오류가 발생했습니다.");
      List<Notification> list = memberMapper.findNotificationByUserId(user.getId());
      List<NotificationDto> dtoList = list.stream().map(NotificationDto::new).toList();

      return dtoList;
   }

   // 멤버 조회
   @GetMapping("/list")
   public List<String> getMember(Long routeId, Principal principal){
      User user = userMapper.findByUsername(principal.getName());
      if(user==null)
         throw new RuntimeException("오류가 발생했습니다.");
      return memberService.getMember(routeId);

   }

}
