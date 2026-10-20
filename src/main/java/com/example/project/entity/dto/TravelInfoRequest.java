package com.example.project.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelInfoRequest {
    private String city;
    private String destination;
    private String accommodation;
    private String schedule;
    private String purpose;
}
