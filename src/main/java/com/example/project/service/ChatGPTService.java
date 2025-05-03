package com.example.project.service;

import com.example.project.config.WebClientConfig;
import com.example.project.entity.dto.RouteRequest;
import com.example.project.entity.dto.TravelRequest;
import com.example.project.entity.dto.TravelResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatGPTService {

   private final WebClient OpenAiWebClient;

   private String escapeJson(String text) {
      return text.replace("\"", "\\\"")
            .replace("\n", "\\n");
   }

   public String OpenAPISetting(String prompt,int maxToken){
      String url= "https://api.openai.com/v1/chat/completions";
      String requestBody = String.format("{\n" +
            "  \"model\": \"gpt-4o\",\n" +
            "  \"messages\": [\n" +
            "    {\"role\": \"user\", \"content\": \"%s\"}\n" +
            "  ],\n" +
            "  \"max_tokens\": %d \n" +
            "}", prompt, maxToken);
      String apiKey = "Bearer "+System.getenv("SPRING_AI_OPENAI_API_KEY");

      String response = OpenAiWebClient.post()
            .uri(url)
            .header("Authorization", apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class).block();
      return response;

   }
   // 여행지 추천 받기
   public List<TravelResponse> getTravelRecommendations(TravelRequest request) throws JsonProcessingException {
      String prompt = escapeJson(recommendedPrompt(request));
      String content = extractContent(OpenAPISetting(prompt,400));
      return parseRecommendedPlaces(content);
   }

   private String recommendedPrompt(TravelRequest request){
      return String.format("다음 정보로 여행지를 추천해주세요:\n"+
              "일정 정보: %s \n" +
              "상세 인원 정보: %s \n" +
              "여행의 목적: %s \n" +
              "여행 예산: %s \n" +
              "선호하는 숙박 시설: %s \n" +
              "중요하게 여기는 요소: %s \n" +
              "선호하는 이동 수단: %s \n" +
              "특히 마음에 들었던 여행지: %s \n" +
              "주의해야 할 요소: %s \n" +
              "AI 추천 방식: %s \n" +
              "여행 중 어느 정도의 자유 시간을 원하시나요? : %s \n" +
              "\n" +
              "위 정보를 바탕으로, 무조건 어떤 부가적인 말 없이 다음과 같은 형식으로 국내 여행지 7곳을 추천해주세요.\n" +
              "1. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3\n" +
              "2. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3\n" +
              "3. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3\n" +
              "4. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3\n" +
              "5. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3\n" +
              "6. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3\n" +
              "7. [지역명] - [도시명]: 해당 도시의 특징 1, 특징 2, 특징 3",
              request.getSchedule(),
              request.getGroupComposition(),
              request.getPurpose(),
              request.getBudget(),
              request.getAccommodation(),
              request.getKeyPoint(),
              request.getTransport(),
              request.getFavorite(),
              request.getSpecialNeeds(),
              request.getRecommendationType(),
              request.getFreeTime()
      );
   }

   // 여행 루트 추천
   public String getRouteRecommendation(RouteRequest routeRequest) throws JsonProcessingException {
      String prompt = escapeJson(RoutePrompt(routeRequest));
      String content = extractContent(OpenAPISetting(prompt,1000));
      return content; //수정
   }

   public String RoutePrompt(RouteRequest routeRequest){
      return String.format(
         "일정 정보: %s \n" +
         "상세 인원 정보: %s \n" +
         "여행의 목적: %s \n" +
         "여행 예산: %s \n" +
         "선호하는 숙박 시설: %s \n" +
         "중요하게 여기는 요소: %s \n" +
         "선호하는 이동 수단: %s \n" +
         "특히 마음에 들었던 여행지: %s \n" +
         "주의해야 할 요소: %s \n" +
         "AI 추천 방식: %s \n" +
         "여행 중 어느 정도의 자유 시간을 원하시나요? : %s \n" +

         "여행 장소: %s \n" +
         "특징: %s, %s, %s \n" +

         "위에서 언급된 각 여행지에 대해 여행 루트를 작성합니다."+
         "하루에 두 가지 일정과 해당 여행지 유명 음식 두 가지를 한 줄에 작성해주세요."+
         "부가적인 말은 제외하고 다음 정보를 다음과 동일하게 제공해 주세요:"+
         "[n일차] (e.g. [1일차]) \n" +
         "- 첫 번째 장소명:\n" +
         "- 영업 시간:\n" +
         "- 특별한 방문 팁:\n" +

         "- 두 번째 장소명:\n" +
         "- 영업 시간:\n" +
         "- 특별한 방문 팁:\n" +

         "- 유명 음식:\n",
            routeRequest.getTravelRequest().getSchedule(),
            routeRequest.getTravelRequest().getGroupComposition(),
            routeRequest.getTravelRequest().getPurpose(),
            routeRequest.getTravelRequest().getBudget(),
            routeRequest.getTravelRequest().getAccommodation(),
            routeRequest.getTravelRequest().getKeyPoint(),
            routeRequest.getTravelRequest().getTransport(),
            routeRequest.getTravelRequest().getFavorite(),
            routeRequest.getTravelRequest().getSpecialNeeds(),
            routeRequest.getTravelRequest().getRecommendationType(),
            routeRequest.getTravelRequest().getFreeTime(),
            routeRequest.getDestination(),
            routeRequest.getFeatures().get(0),
            routeRequest.getFeatures().get(1),
            routeRequest.getFeatures().get(2)
      );
   }


   public List<TravelResponse> parseRecommendedPlaces(String content) {
      List<TravelResponse> places = new ArrayList<>();
      String[] lines = content.split("\n");
      for (String line : lines) {
         String destination = extractPlaceDestination(line);
         List<String> features = extractFeatures(line);
         places.add(new TravelResponse(destination, features));
      }
      return places;
   }
   public String extractContent(String response) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(response);
      JsonNode choices = root.path("choices");
      if (choices.isArray() && choices.size() > 0) {
         JsonNode message = choices.get(0).path("message");
         return message.path("content").asText();
      }
      return null;
   }
   public String extractPlaceDestination(String content){
      int spotIndex = content.indexOf('.');
      int colonIndex = content.indexOf(':');
      content = content.substring(spotIndex+1, colonIndex);
      return content.trim();
   }

   public List<String> extractFeatures(String content){
      int colonIndex = content.indexOf(':');
      content = content.substring(colonIndex+1);
      String[] features=content.split(",");
      return Arrays.stream(features).map(String::trim).collect(Collectors.toList());
   }

}
