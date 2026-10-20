package com.example.project.entity.dto;

import lombok.Data;

@Data
public class BlockMoveMessage {
   private long routeId;
   private long blockId;
   private long targetBlockId;
   private boolean insertAfter;
}
