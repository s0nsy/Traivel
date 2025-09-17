package com.example.project.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class TravelRequest {
   @Schema(description="여행 일정 정보", example="2025-05-01 부터 2025-05-05까지")
   private String schedule;
   @Schema(description="여행 목적", example="맛집")
   private String purpose;
   @Schema(description="예산", example="50만원")
   private String budget;
   @Schema(description="선호하는 숙박 시설", example="호텔")
   private String accommodation;
   @Schema(description="선호하는 장소 및 중요 요소", example="바다 뷰 숙소")
   private String keyPoint;
   @Schema(description="이동 방식",example="자동차")
   private String transport;
   @Schema(description="여행 구성원",example="친구")
   private String companion;
   @Schema(description="선호하는 여행지", example = "전라남도 여수")
   private String favorite;
   @Schema(description="특이 사항", example="휠체어 이동 가능 경로")
   private String specialNeeds;
   @Schema(description="추천 방식", example="친환경적인 여행지 추천")
   private String recommendationType;
   @Schema(description="자유시간", example="하루에 두 시간")
   private String freeTime;

}
