package com.example.project.config;

import com.example.project.config.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("WebSocket CONNECT: Authorization 헤더 없음");
            throw new IllegalArgumentException("WebSocket 연결에는 인증 토큰이 필요합니다.");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateAccessToken(token)) {
            log.warn("WebSocket CONNECT: 유효하지 않은 토큰");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String username = jwtUtil.getUsernameFromToken(token);
        accessor.setUser(() -> username);
        log.debug("WebSocket CONNECT 인증 성공: user={}", username);

        return message;
    }
}
