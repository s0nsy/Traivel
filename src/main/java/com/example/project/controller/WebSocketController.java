package com.example.project.controller;

import com.example.project.entity.dto.BlockDto;
import com.example.project.entity.dto.BlockMoveMessage;
import com.example.project.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
   private final RouteService routeService;
   private final SimpMessagingTemplate messagingTemplate;

   // 루트 공유
   @MessageMapping("/move-block")
   @ResponseBody
   public void moveBlock(BlockMoveMessage message){
      System.out.println("블록 이동 요청 받음: " + message.getBlockId() + " -> " + message.getNewOrder());
      BlockDto updated=routeService.moveBlock(message.getRouteId(),message.getBlockId(),message.getNewOrder(), message.getNewDay());
      if(updated!=null){
         messagingTemplate.convertAndSend("/topic/blocks", routeService.getAllBlocks(message.getRouteId()));
      }
   }
   @GetMapping("/ws")
   public String webSocket(){
      return "webSocket";
   }

}
