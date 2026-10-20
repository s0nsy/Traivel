package com.example.project.controller;

import com.example.project.config.SecurityConfig;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.Access;
import com.example.project.entity.User;
import com.example.project.repository.AccessRepository;
import com.example.project.repository.ImageRepository;
import com.example.project.service.AccessService;
import com.example.project.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessController.class)
@Import(SecurityConfig.class)
class AccessControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean AccessService accessService;
    @MockitoBean AccessRepository accessRepository;
    @MockitoBean ImageRepository imageRepository;
    @MockitoBean JwtUtil jwtUtil;
    @MockitoBean CustomUserDetailsService customUserDetailsService;
    @MockitoBean JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── 후기 목록 조회 ───────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void showAccess_success() throws Exception {
        User writer = new User();
        writer.setUsername("writer1");

        Access access = new Access();
        access.setTitle("제주 여행 후기");
        access.setWriter(writer);

        given(accessRepository.findAll()).willReturn(List.of(access));

        mockMvc.perform(get("/access"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void showAccess_emptyList() throws Exception {
        given(accessRepository.findAll()).willReturn(List.of());

        mockMvc.perform(get("/access"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── 후기 상세 조회 ───────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getAccess_success() throws Exception {
        User writer = new User();
        writer.setUsername("writer1");

        Access access = new Access();
        access.setTitle("제주 여행 후기");
        access.setContent("좋았어요");
        access.setWriter(writer);

        given(accessRepository.findById(1L)).willReturn(Optional.of(access));
        given(imageRepository.findIdsByAccessId(1L)).willReturn(List.of(10L, 11L));

        mockMvc.perform(get("/access/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제주 여행 후기"))
                .andExpect(jsonPath("$.writer").value("writer1"));
    }

    @Test
    @WithMockUser
    void getAccess_notFound_throws() throws Exception {
        given(accessRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/access/999"))
                .andExpect(status().is5xxServerError());
    }

    // ── 후기 작성 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void createAccess_success() throws Exception {
        doNothing().when(accessService).createAccess(any(), eq("testuser"));

        mockMvc.perform(multipart("/access/write")
                        .with(csrf())
                        .param("title", "새 후기")
                        .param("content", "내용입니다"))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글을 작성했습니다."));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAccess_missingTitle_returns400() throws Exception {
        mockMvc.perform(multipart("/access/write")
                        .with(csrf())
                        .param("content", "내용만 있음"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAccess_withImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "imageUrl", "test.jpg", "image/jpeg", "image-data".getBytes());
        doNothing().when(accessService).createAccess(any(), eq("testuser"));

        mockMvc.perform(multipart("/access/write")
                        .file(image)
                        .with(csrf())
                        .param("title", "이미지 포함 후기")
                        .param("content", "내용"))
                .andExpect(status().isOk());
    }

    @Test
    void createAccess_unauthenticated_returns401() throws Exception {
        mockMvc.perform(multipart("/access/write")
                        .with(csrf())
                        .param("title", "후기")
                        .param("content", "내용"))
                .andExpect(status().is4xxClientError());
    }

    // ── 후기 수정 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void editAccess_success() throws Exception {
        doNothing().when(accessService).editAccess(any(), eq(1L), eq("testuser"));

        mockMvc.perform(multipart("/access/1")
                        .with(csrf())
                        .param("title", "수정된 후기")
                        .param("content", "수정된 내용")
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글을 수정했습니다."));
    }

    // ── 후기 삭제 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void deleteAccess_success() throws Exception {
        doNothing().when(accessService).deleteAccess(eq(1L), eq("testuser"));

        mockMvc.perform(delete("/access/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글을 삭제했습니다."));
    }

    @Test
    void deleteAccess_unauthenticated_returns401() throws Exception {
        mockMvc.perform(delete("/access/1").with(csrf()))
                .andExpect(status().is4xxClientError());
    }
}
