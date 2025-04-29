package com.example.project.service;

import com.example.project.entity.dto.TravelRequest;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatGPTService {

   public ChatClient chatClient;

   public String getTravelRecommendations(TravelRequest request){
      String prompt = createPrompt(request);

      String response= chatClient.prompt()
              .user(prompt)
              .call()
              .content();

      return response;
   }

   private String createPrompt(TravelRequest request){
      return String.format("다음 정보로 여행지를 추천해주세요:\n"+
              "일정 정보: %s \n" +
              "상세 인원 정보: %s \n" +
              "여행의 목적: %s \n" +
              "여행 예산: %s \n" +
              "여행 중 가장 중요하게 생각하는 요소: %s \n" +
              "선호하는 숙박 시설: %s \n" +
              "선호하는 이동 수단: %s \n" +
              "특히 마음에 들었던 여행지: %s \n" +
              "필요하거나 피하고 싶은 요소: %s \n" +
              "AI 추천 방식: %s \n" +
              "여행 중 어느 정도의 자유 시간을 원하시나요? : %s \n" +
              "\n" +
              "위 정보를 바탕으로, 무조건 어떤 부가적인 말 없이 다음과 같은 형식으로 국내 여행지 7곳을 추천해주세요.\n" +
              "1. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3\n" +
              "2. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3\n" +
              "3. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3\n" +
              "4. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3\n" +
              "5. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3\n" +
              "6. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3\n" +
              "7. [지역명] - [도시명]: 특징 1, 특징 2, 특징 3",
              request.getSchedule(),
              request.getGroupComposition(),
              request.getPurpose(),
              request.getBudget(),
              request.getKeyPoint(),
              request.getAccommodation(),
              request.getTransport(),
              request.getFavorite(),
              request.getSpecialNeeds(),
              request.getRecommendationType(),
              request.getFreeTime()
      );
   }
}
