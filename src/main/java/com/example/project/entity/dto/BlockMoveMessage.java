package com.example.project.entity.dto;

import lombok.Data;

@Data
public class BlockMoveMessage {
   private long blockId;
   private int newDay;
   private int newOrder;
   private long routeId;
}
