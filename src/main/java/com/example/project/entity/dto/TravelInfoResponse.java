package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TravelInfoResponse {
    private List<NaverPlaceDto> hotels;
    private List<HighlightDto> highlights;
}
