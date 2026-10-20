package com.example.project.controller;

import com.example.project.config.SecurityConfig;
import com.example.project.config.security.dto.LoginRequest;
import com.example.project.config.security.dto.LoginResponse;
import com.example.project.config.security.dto.RefreshRequest;
import com.example.project.config.security.dto.RegisterRequest;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Notification;
import com.example.project.repository.NotificationRepository;
import com.example.project.service.CustomUserDetailsService;
import com.example.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean UserService userService;
    @MockitoBean NotificationRepository notificationRepository;
    @MockitoBean JwtUtil jwtUtil;
    @MockitoBean CustomUserDetailsService customUserDetailsService;
    @MockitoBean JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── 회원가입 ─────────────────────────────────────────────────────────────

    @Test
    void register_success() throws Exception {
        RegisterRequest req = new RegisterRequest("testuser1", "password123", "password123", "test@example.com");
        LoginResponse loginResp = new LoginResponse("access-token", "refresh-token");

        doNothing().when(userService).register(any());
        given(userService.login(any())).willReturn(loginResp);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void register_blankUsername_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("", "password123", "password123", "test@example.com");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_usernameTooShort_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("ab", "password123", "password123", "test@example.com");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_invalidUsernamePattern_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("user name!", "password123", "password123", "test@example.com");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_passwordTooShort_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("testuser2", "short", "short", "test@example.com");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_invalidEmail_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("testuser3", "password123", "password123", "notanemail");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── 로그인 ───────────────────────────────────────────────────────────────

    @Test
    void login_success() throws Exception {
        LoginRequest req = new LoginRequest("testuser", "password123");
        LoginResponse resp = new LoginResponse("access-token", "refresh-token");
        given(userService.login(any())).willReturn(resp);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void login_blankUsername_returns400() throws Exception {
        LoginRequest req = new LoginRequest("", "password123");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_blankPassword_returns400() throws Exception {
        LoginRequest req = new LoginRequest("testuser", "");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── 토큰 재발급 ──────────────────────────────────────────────────────────

    @Test
    void refresh_success() throws Exception {
        RefreshRequest req = new RefreshRequest();
        req.setRefreshToken("valid-refresh-token");
        LoginResponse resp = new LoginResponse("new-access", "new-refresh");
        given(userService.refresh("valid-refresh-token")).willReturn(resp);

        mockMvc.perform(post("/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access"));
    }

    @Test
    void refresh_blankToken_returns400() throws Exception {
        RefreshRequest req = new RefreshRequest();
        req.setRefreshToken("");

        mockMvc.perform(post("/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── 로그아웃 ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void logout_authenticated_success() throws Exception {
        doNothing().when(userService).logout("testuser");

        mockMvc.perform(post("/auth/logout").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 되었습니다."));
    }

    @Test
    void logout_unauthenticated_returns401() throws Exception {
        mockMvc.perform(post("/auth/logout").with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    // ── 알림 조회 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void notifications_success() throws Exception {
        given(userService.notification("testuser")).willReturn(List.of(new Notification()));

        mockMvc.perform(get("/auth/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
