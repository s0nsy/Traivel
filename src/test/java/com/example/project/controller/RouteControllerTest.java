package com.example.project.controller;

import com.example.project.config.SecurityConfig;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.entity.User;
import com.example.project.entity.dto.BlockDto;
import com.example.project.entity.dto.MemoRequest;
import com.example.project.service.CustomUserDetailsService;
import com.example.project.service.RouteService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteController.class)
@Import(SecurityConfig.class)
class RouteControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean RouteService routeService;
    @MockitoBean UserService userService;
    @MockitoBean JwtUtil jwtUtil;
    @MockitoBean CustomUserDetailsService customUserDetailsService;
    @MockitoBean JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── 루트 생성 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "testuser")
    void addRoute_success() throws Exception {
        given(routeService.addRoute("testuser", "제주도 여행")).willReturn(1L);

        mockMvc.perform(post("/route")
                        .with(csrf())
                        .param("title", "제주도 여행"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void addRoute_unauthenticated_returns401() throws Exception {
        mockMvc.perform(post("/route").with(csrf()).param("title", "test"))
                .andExpect(status().is4xxClientError());
    }

    // ── 루트 조회 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getRoute_success() throws Exception {
        BlockDto block = new BlockDto();
        given(routeService.getAllBlocks(1L)).willReturn(List.of(block));

        mockMvc.perform(get("/route/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getRoute_notFound_returns404() throws Exception {
        given(routeService.getAllBlocks(999L))
                .willThrow(new com.example.project.exception.NotFoundException(
                        com.example.project.exception.ErrorCode.ROUTE_NOT_FOUND));

        mockMvc.perform(get("/route/999"))
                .andExpect(status().is4xxClientError());
    }

    // ── 메모 추가 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void addMemo_success() throws Exception {
        MemoRequest req = new MemoRequest(1L, "메모 내용", 1, 1, "MEMO");
        doNothing().when(routeService).addMemo(any());

        mockMvc.perform(post("/route/memo")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("메모를 추가했습니다."));
    }

    @Test
    @WithMockUser
    void addMemo_nullRouteId_returns400() throws Exception {
        MemoRequest req = new MemoRequest(null, "내용", 1, 1, "MEMO");

        mockMvc.perform(post("/route/memo")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void addMemo_blankContent_returns400() throws Exception {
        MemoRequest req = new MemoRequest(1L, "", 1, 1, "MEMO");

        mockMvc.perform(post("/route/memo")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void addMemo_dayZero_returns400() throws Exception {
        MemoRequest req = new MemoRequest(1L, "내용", 1, 0, "MEMO");

        mockMvc.perform(post("/route/memo")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── 메모 수정 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void editMemo_success() throws Exception {
        doNothing().when(routeService).editMemo(eq(1L), eq("수정된 내용"), eq("username"));

        mockMvc.perform(put("/route/memo/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("content", "수정된 내용"))))
                .andExpect(status().isOk())
                .andExpect(content().string("수정했습니다."));
    }

    // ── 메모 삭제 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void deleteMemo_success() throws Exception {
        given(routeService.deleteMemo(1L)).willReturn("삭제되었습니다");

        mockMvc.perform(delete("/route/memo/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("삭제되었습니다"));
    }

    // ── 장소 삭제 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void deletePlace_success() throws Exception {
        given(routeService.deletePlace(1L)).willReturn("삭제되었습니다");

        mockMvc.perform(delete("/route/place/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("삭제되었습니다"));
    }

    // ── 루트 삭제 ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "owner")
    void deleteRoute_success() throws Exception {
        User user = new User();
        user.setUsername("owner");
        given(userService.findByUsername("owner")).willReturn(user);
        doNothing().when(routeService).deleteRoute(eq(1L), any(User.class));

        mockMvc.perform(delete("/route/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("루트를 삭제했습니다."));
    }

    @Test
    void deleteRoute_unauthenticated_returns401() throws Exception {
        mockMvc.perform(delete("/route/1").with(csrf()))
                .andExpect(status().is4xxClientError());
    }
}
