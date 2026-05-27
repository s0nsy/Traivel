package com.example.project.controller;

import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RefreshRequest;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.entity.dto.NotificationRequest;
import com.example.project.repository.NotificationRepository;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("회원가입 요청: {}", registerRequest.getUsername());
        userService.register(registerRequest);
        LoginResponse response = userService.login(
                new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));
        return ResponseEntity.ok(Map.of(
                "message", "Registered",
                "accessToken", response.getAccessToken(),
                "refreshToken", response.getRefreshToken()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청: {}", request.getUsername());
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(description = "refresh token으로 새 access token과 refresh token을 발급합니다.")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        log.info("토큰 재발급 요청");
        return ResponseEntity.ok(userService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("로그아웃 요청: {}", userDetails.getUsername());
        userService.logout(userDetails.getUsername());
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> notifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("알림 목록 조회: {}", userDetails.getUsername());
        return ResponseEntity.ok(userService.notification(userDetails.getUsername()));
    }

    @PostMapping("/notifications")
    @Operation(description = "관리자만 알림을 보낼 수 있습니다.")
    public ResponseEntity<String> addNotification(
            Long userId,
            @RequestBody NotificationRequest notification,
            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        log.info("관리자 알림 전송 요청: 대상 userId={}, 요청자={}", userId, userDetails.getUsername());
        User user = userService.findByUsername(userDetails.getUsername());
        if (!user.getRole().equals("ROLE_ADMIN")) {
            log.warn("알림 전송 권한 없음: 요청자={}", userDetails.getUsername());
            throw new AccessDeniedException("관리자가 아닙니다.");
        }
        Notification newNotification = new Notification();
        newNotification.setUserId(userId);
        newNotification.setMessage(notification.getMessage());
        newNotification.setType(notification.getType());
        newNotification.setRead(notification.isRead());
        notificationRepository.save(newNotification);
        log.info("관리자 알림 전송 완료: 대상 userId={}", userId);
        return ResponseEntity.ok("알림 추가했습니다.");
    }
}
