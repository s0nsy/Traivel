package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TravelResponse {
   private String destination;
   private List<String> features;

}
