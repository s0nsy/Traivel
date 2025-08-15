package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class TravelResponse implements Serializable {
   private String destination;
   private List<String> features;

}
