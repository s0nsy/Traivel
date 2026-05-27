package com.example.project.service;

import com.example.project.entity.Notification;
import com.example.project.entity.RefreshToken;
import com.example.project.entity.User;
import com.example.project.exception.DuplicateException;
import com.example.project.exception.ErrorCode;
import com.example.project.exception.InvalidValueException;
import com.example.project.exception.NotFoundException;
import com.example.project.repository.NotificationRepository;
import com.example.project.repository.RefreshTokenRepository;
import com.example.project.repository.UserRepository;
import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.config.security.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;

    public User findByUsername(String username) {
        log.debug("사용자 조회: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void register(RegisterRequest request) {
        log.info("회원가입 요청: {}", request.getUsername());

        if (!request.getPassword().equals(request.getConfirmedPassword())) {
            log.warn("회원가입 실패 - 비밀번호 불일치: {}", request.getUsername());
            throw new InvalidValueException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("회원가입 실패 - 중복 아이디: {}", request.getUsername());
            throw new DuplicateException(ErrorCode.DUPLICATE_USERNAME);
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRole("ROLE_USER");
        userRepository.save(newUser);
        log.info("회원가입 완료: {}", request.getUsername());
    }

    public LoginResponse login(LoginRequest request) {
        log.info("로그인 시도: {}", request.getUsername());

        userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("로그인 실패 - 존재하지 않는 사용자: {}", request.getUsername());
                    return new NotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String username = authentication.getName();
        String accessToken = jwtUtil.createAccessToken(username);
        String refreshToken = jwtUtil.createRefreshToken(username);

        refreshTokenRepository.deleteByUsername(username);
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtUtil.getRefreshTokenExpireTime() / 1000);
        refreshTokenRepository.save(new RefreshToken(refreshToken, username, expiresAt));

        log.info("로그인 성공: {}", username);
        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshTokenValue) {
        if (!jwtUtil.validateRefreshToken(refreshTokenValue)) {
            log.warn("유효하지 않은 refresh token");
            throw new InvalidValueException(ErrorCode.INVALID_TOKEN);
        }

        RefreshToken stored = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 refresh token");
                    return new InvalidValueException(ErrorCode.INVALID_TOKEN);
                });

        if (stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            log.warn("만료된 refresh token: {}", stored.getUsername());
            throw new InvalidValueException(ErrorCode.EXPIRED_TOKEN);
        }

        String username = stored.getUsername();
        String newAccessToken = jwtUtil.createAccessToken(username);
        String newRefreshToken = jwtUtil.createRefreshToken(username);

        refreshTokenRepository.delete(stored);
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtUtil.getRefreshTokenExpireTime() / 1000);
        refreshTokenRepository.save(new RefreshToken(newRefreshToken, username, expiresAt));

        log.info("토큰 재발급 완료: {}", username);
        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String username) {
        log.info("로그아웃: {}", username);
        refreshTokenRepository.deleteByUsername(username);
    }

    public List<Notification> notification(String username) {
        log.debug("알림 조회: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return notificationRepository.findByUserId(user.getId());
    }
}
