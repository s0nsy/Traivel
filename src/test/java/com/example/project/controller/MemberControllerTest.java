package com.example.project.controller;

import com.example.project.config.SecurityConfig;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Notification;
import com.example.project.entity.User;
import com.example.project.repository.NotificationRepository;
import com.example.project.service.CustomUserDetailsService;
import com.example.project.service.MemberService;
import com.example.project.service.RouteService;
import com.example.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean RouteService routeService;
    @MockitoBean UserService userService;
    @MockitoBean MemberService memberService;
    @MockitoBean NotificationRepository notificationRepository;
    @MockitoBean JwtUtil jwtUtil;
    @MockitoBean CustomUserDetailsService customUserDetailsService;
    @MockitoBean JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private User mockUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setRole("ROLE_USER");
        return user;
    }

    // ── 초대 링크 생성 ───────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "owner")
    void createLink_success() throws Exception {
        given(memberService.createInviteLink(1L, "owner")).willReturn("invite-token-uuid");

        mockMvc.perform(post("/member/link")
                        .with(csrf())
                        .param("routeId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("invite-token-uuid"));
    }

    @Test
    void createLink_unauthenticated_returns401() throws Exception {
        mockMvc.perform(post("/member/link").with(csrf()).param("routeId", "1"))
                .andExpect(status().is4xxClientError());
    }

    // ── 루트 참가 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "newmember")
    void joinRoute_success() throws Exception {
        User user = mockUser("newmember");
        given(userService.findByUsername("newmember")).willReturn(user);
        doNothing().when(routeService).joinRoute(eq("invite-uuid"), eq(user));

        mockMvc.perform(get("/member/invite/invite-uuid"))
                .andExpect(status().isOk())
                .andExpect(content().string("정상적으로 초대되었습니다."));
    }

    @Test
    void joinRoute_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/member/invite/some-token"))
                .andExpect(status().is4xxClientError());
    }

    // ── 멤버 방출 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "owner")
    void deleteMember_success() throws Exception {
        User target = mockUser("removeme");
        given(userService.findByUsername("removeme")).willReturn(target);
        doNothing().when(memberService).deleteMember(eq(1L), eq("owner"), eq(target));

        mockMvc.perform(delete("/member/eviction/member")
                        .with(csrf())
                        .param("routeId", "1")
                        .param("username", "removeme"))
                .andExpect(status().isOk())
                .andExpect(content().string("removeme을 퇴장시켰습니다."));
    }

    // ── 알림 조회 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void getNotification_success() throws Exception {
        User user = mockUser("testuser");
        given(userService.findByUsername("testuser")).willReturn(user);

        Notification n = new Notification();
        n.setMessage("테스트 알림");
        n.setType("INFO");
        given(notificationRepository.findByUserId(any())).willReturn(List.of(n));

        mockMvc.perform(get("/member/notification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].message").value("테스트 알림"));
    }

    // ── 멤버 목록 조회 ───────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void getMember_success() throws Exception {
        User user = mockUser("testuser");
        given(userService.findByUsername("testuser")).willReturn(user);
        given(memberService.getMember(1L)).willReturn(List.of("testuser", "other"));

        mockMvc.perform(get("/member/list/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("testuser"))
                .andExpect(jsonPath("$[1]").value("other"));
    }

    @Test
    void getMember_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/member/list/1"))
                .andExpect(status().is4xxClientError());
    }
}
