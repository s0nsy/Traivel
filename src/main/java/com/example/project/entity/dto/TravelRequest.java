package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TravelRequest {
   private String schedule;
   private String groupComposition;
   private String purpose;
   private String budget;
   private String keyPoint;
   private String accommodation;
   private String transport;
   private String favorite;
   private String specialNeeds;
   private String recommendationType;
   private String freeTime;

}
