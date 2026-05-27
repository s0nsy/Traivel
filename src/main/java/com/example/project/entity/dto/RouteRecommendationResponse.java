package com.example.project.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RouteRecommendationResponse {

    private String destination;
    private List<DayRoute> days;

    @Data
    @NoArgsConstructor
    public static class DayRoute {
        private int day;
        private PlaceInfo firstPlace;
        private PlaceInfo secondPlace;
        private List<String> foods;
    }

    @Data
    @NoArgsConstructor
    public static class PlaceInfo {
        private String name;
        private String hours;
        private String tip;
    }
}
