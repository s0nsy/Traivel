package com.example.project.controller;

import com.example.project.entity.dto.BlockDto;
import com.example.project.entity.dto.BlockMoveMessage;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final RouteService routeService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move-block")
    public void moveBlock(BlockMoveMessage message, Principal principal) {
        log.info("블록 이동 요청: 블록ID={}, 루트ID={}, targetBlockId={}, insertAfter={}, user={}",
                message.getBlockId(), message.getRouteId(), message.getTargetBlockId(), message.isInsertAfter(), principal.getName());

        Collection<BlockDto> latestBlocks = routeService.moveBlock(
                message.getRouteId(), message.getBlockId(), message.getTargetBlockId(), message.isInsertAfter(), principal.getName());

        messagingTemplate.convertAndSend("/topic/route/" + message.getRouteId(), latestBlocks);
        log.debug("블록 이동 브로드캐스트 완료: 루트ID={}", message.getRouteId());
    }

    @GetMapping("/ws-test")
    public String webSocket() {
        return "webSocket";
    }

    @GetMapping("/map-test")
    public String showMapTest() {
        return "map-test";
    }
}
