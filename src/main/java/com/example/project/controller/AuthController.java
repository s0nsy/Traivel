package com.example.project.controller;

import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.entity.dto.NotificationRequest;
import com.example.project.repository.NotificationRepository;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final NotificationRepository notificationRepository;
    private final JwtUtil jwtUtil;

    @Value("${cookie.secure:true}")
    private boolean secureCookie;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
                                      HttpServletResponse response) {
        log.info("회원가입 요청: {}", registerRequest.getUsername());
        userService.register(registerRequest);
        LoginResponse tokens = userService.login(
                new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));
        addTokenCookies(response, tokens);
        return ResponseEntity.ok(Map.of("username", registerRequest.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                   HttpServletResponse response) {
        log.info("로그인 요청: {}", request.getUsername());
        LoginResponse tokens = userService.login(request);
        addTokenCookies(response, tokens);
        return ResponseEntity.ok(Map.of("username", request.getUsername()));
    }

    @PostMapping("/refresh")
    @Operation(description = "refresh token 쿠키로 새 access/refresh token 쿠키를 발급합니다.")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        log.info("토큰 재발급 요청");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token missing");
        }
        LoginResponse tokens = userService.refresh(refreshToken);
        addTokenCookies(response, tokens);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails,
                                         HttpServletResponse response) {
        log.info("로그아웃 요청: {}", userDetails.getUsername());
        userService.logout(userDetails.getUsername());
        clearTokenCookies(response);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    private void addTokenCookies(HttpServletResponse response, LoginResponse tokens) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(
                "accessToken", tokens.getAccessToken(),
                Duration.ofMillis(jwtUtil.getAccessTokenExpireTime())).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(
                "refreshToken", tokens.getRefreshToken(),
                Duration.ofMillis(jwtUtil.getRefreshTokenExpireTime())).toString());
    }

    private void clearTokenCookies(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE,
                buildCookie("accessToken", "", Duration.ZERO).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                buildCookie("refreshToken", "", Duration.ZERO).toString());
    }

    private ResponseCookie buildCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();
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
