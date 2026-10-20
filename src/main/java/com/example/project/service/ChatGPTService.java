package com.example.project.service;

import com.example.project.entity.dto.HighlightDto;
import com.example.project.entity.dto.MemoRequest;
import com.example.project.entity.dto.RecommendRouteRequest;
import com.example.project.entity.dto.RouteRecommendationResponse;
import com.example.project.entity.dto.RouteRecommendationResponse.DayRoute;
import com.example.project.entity.dto.TravelInfoRequest;
import com.example.project.entity.dto.TravelRequest;
import com.example.project.entity.dto.TravelResponse;
import com.example.project.exception.BusinessException;
import com.example.project.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ChatGPTService {

    private final WebClient OpenAiWebClient;
    private final RouteService routeService;
    private final ObjectMapper objectMapper;

    private String callOpenAI(String prompt, int maxToken) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", "gpt-4o");
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            body.put("response_format", Map.of("type", "json_object"));
            body.put("max_tokens", maxToken);

            String requestBody = objectMapper.writeValueAsString(body);

            return OpenAiWebClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (JsonProcessingException e) {
            log.error("OpenAI 요청 직렬화 실패", e);
            throw new BusinessException(ErrorCode.OPENAI_RESPONSE_INVALID);
        }
    }

    private String extractContent(String apiResponse) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(apiResponse);
        JsonNode choices = root.path("choices");
        if (choices.isArray() && !choices.isEmpty()) {
            String content = choices.get(0).path("message").path("content").asText(null);
            if (content != null && !content.isBlank()) {
                return content;
            }
        }
        log.error("OpenAI 응답에서 content 추출 실패: {}", apiResponse);
        throw new BusinessException(ErrorCode.OPENAI_RESPONSE_INVALID);
    }

    @Cacheable(value = "추천받은여행지", key = "@cacheKeyUtil.getTravelRecommKey(#request)")
    public List<TravelResponse> getTravelRecommendations(TravelRequest request) throws JsonProcessingException {
        log.info("여행지 추천 요청: 일정={}, 목적={}", request.getSchedule(), request.getPurpose());

        String content = extractContent(callOpenAI(recommendedPrompt(request), 800));
        JsonNode root = objectMapper.readTree(content);

        List<TravelResponse> results = new ArrayList<>();
        for (JsonNode rec : root.path("recommendations")) {
            String destination = rec.path("destination").asText();
            String city = rec.path("city").asText();
            List<String> features = new ArrayList<>();
            for (JsonNode f : rec.path("features")) {
                features.add(f.asText());
            }
            results.add(new TravelResponse(destination, city, features));
        }

        log.info("여행지 추천 완료: 추천 수={}", results.size());
        return results;
    }

    private String recommendedPrompt(TravelRequest r) {
        return String.format(
                "다음 여행 정보를 바탕으로 국내 여행지 7곳을 추천해주세요.\n" +
                "일정: %s\n목적: %s\n예산: %s\n숙박: %s\n중요요소: %s\n" +
                "이동수단: %s\n구성원: %s\n선호지: %s\n특이사항: %s\n추천방식: %s\n자유시간: %s\n\n" +
                "서울, 제주처럼 넓은 지역을 추천할 경우 city에는 '홍대', '강남', '애월', '성산'처럼 실제 머무를 세부 지역을 입력하세요.\n" +
                "반드시 아래 JSON 형식으로만 응답하세요. JSON 외의 텍스트는 절대 포함하지 마세요.\n" +
                "{\n" +
                "  \"recommendations\": [\n" +
                "    {\"destination\": \"경상북도\", \"city\": \"경주\", \"features\": [\"역사유적\", \"불국사\", \"첨성대\"]},\n" +
                "    {\"destination\": \"전라남도\", \"city\": \"여수\", \"features\": [\"바다뷰\", \"밤바다\", \"케이블카\"]},\n" +
                "    {\"destination\": \"제주특별자치도\", \"city\": \"애월\", \"features\": [\"카페거리\", \"해안도로\", \"노을\"]},\n" +
                "    {\"destination\": \"제주특별자치도\", \"city\": \"성산\", \"features\": [\"일출봉\", \"우도\", \"스노클링\"]}\n" +
                "  ]\n" +
                "}",
                r.getSchedule(), r.getPurpose(), r.getBudget(), r.getAccommodation(),
                r.getKeyPoint(), r.getTransport(), r.getCompanion(), r.getFavorite(),
                r.getSpecialNeeds(), r.getRecommendationType(), r.getFreeTime()
        );
    }

    public RouteRecommendationResponse getRouteRecommendation(RecommendRouteRequest routeRequest)
            throws JsonProcessingException {
        log.info("루트 추천 요청: 목적지={}", routeRequest.getDestination());

        String content = extractContent(callOpenAI(routePrompt(routeRequest), 1500));
        RouteRecommendationResponse result = objectMapper.readValue(content, RouteRecommendationResponse.class);

        log.debug("루트 추천 완료: 목적지={}, 일수={}", routeRequest.getDestination(),
                result.getDays() != null ? result.getDays().size() : 0);
        return result;
    }

    private String routePrompt(RecommendRouteRequest r) {
        TravelRequest t = r.getTravelRequest();
        return String.format(
                "다음 여행 정보를 바탕으로 '%s' 여행 루트를 작성해주세요.\n" +
                "일정: %s\n목적: %s\n예산: %s\n숙박: %s\n중요요소: %s\n이동수단: %s\n" +
                "장소 특징: %s, %s, %s\n\n" +
                "각 day마다 방문할 장소 2곳과 유명 음식 2가지를 포함하세요.\n" +
                "반드시 아래 JSON 형식으로만 응답하세요. JSON 외의 텍스트는 절대 포함하지 마세요.\n" +
                "{\n" +
                "  \"destination\": \"%s\",\n" +
                "  \"days\": [\n" +
                "    {\n" +
                "      \"day\": 1,\n" +
                "      \"firstPlace\": {\"name\": \"오동도\", \"hours\": \"09:00-18:00\", \"tip\": \"아침 방문 추천\"},\n" +
                "      \"secondPlace\": {\"name\": \"이순신광장\", \"hours\": \"상시개방\", \"tip\": \"야경이 아름다움\"},\n" +
                "      \"foods\": [\"돌산갓김치\", \"서대회\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}",
                r.getDestination(),
                t.getSchedule(), t.getPurpose(), t.getBudget(), t.getAccommodation(),
                t.getKeyPoint(), t.getTransport(),
                r.getFeatures().get(0), r.getFeatures().get(1), r.getFeatures().get(2),
                r.getDestination()
        );
    }

    public void adjustRoute(Long routeId, RouteRecommendationResponse routeData) {
        log.info("AI 추천 루트 적용 요청: 루트ID={}", routeId);

        if (routeData.getDays() == null || routeData.getDays().isEmpty()) {
            log.warn("AI 루트 적용 실패 - days 없음: 루트ID={}", routeId);
            return;
        }

        for (DayRoute dayRoute : routeData.getDays()) {
            int day = dayRoute.getDay();

            RouteRecommendationResponse.PlaceInfo fp = dayRoute.getFirstPlace();
            if (fp != null && nameOf(fp) != null && !nameOf(fp).isBlank()) {
                routeService.addPlaceByName(routeId, fp.getName(), fp.getHours(), fp.getTip(), day);
                log.debug("장소 추가: 루트ID={}, day={}, 장소={}", routeId, day, fp.getName());
            }

            RouteRecommendationResponse.PlaceInfo sp = dayRoute.getSecondPlace();
            if (sp != null && nameOf(sp) != null && !nameOf(sp).isBlank()) {
                routeService.addPlaceByName(routeId, sp.getName(), sp.getHours(), sp.getTip(), day);
                log.debug("장소 추가: 루트ID={}, day={}, 장소={}", routeId, day, sp.getName());
            }

            String food1 = foodAt(dayRoute.getFoods(), 0);
            if (food1 != null && !food1.isBlank()) {
                routeService.addMemo(new MemoRequest(routeId, food1, 3, day, "MEMO"));
                log.debug("음식 메모 추가: 루트ID={}, day={}, 음식={}", routeId, day, food1);
            }

            String food2 = foodAt(dayRoute.getFoods(), 1);
            if (food2 != null && !food2.isBlank()) {
                routeService.addMemo(new MemoRequest(routeId, food2, 4, day, "MEMO"));
                log.debug("음식 메모 추가: 루트ID={}, day={}, 음식={}", routeId, day, food2);
            }
        }
        log.info("AI 추천 루트 적용 완료: 루트ID={}", routeId);
    }

    public List<HighlightDto> getHighlights(TravelInfoRequest request) throws JsonProcessingException {
        log.info("현지 트렌드 요청: 도시={}", request.getCity());
        String content = extractContent(callOpenAI(highlightsPrompt(request), 500));
        JsonNode root = objectMapper.readTree(content);
        List<HighlightDto> results = new ArrayList<>();
        for (JsonNode h : root.path("highlights")) {
            results.add(new HighlightDto(h.path("title").asText(), h.path("description").asText()));
        }
        log.info("현지 트렌드 완료: 도시={}, 개수={}", request.getCity(), results.size());
        return results;
    }

    private String highlightsPrompt(TravelInfoRequest r) {
        return String.format(
                "'%s(%s)' 여행의 현지 인기 스팟과 꼭 해볼 것 4가지를 추천해주세요.\n" +
                "여행 목적: %s, 일정: %s\n\n" +
                "반드시 아래 JSON 형식으로만 응답하세요. JSON 외의 텍스트는 절대 포함하지 마세요.\n" +
                "{\"highlights\": [{\"title\": \"스팟명\", \"description\": \"한 줄 설명\"}]}",
                r.getCity(), r.getDestination(), r.getPurpose(), r.getSchedule()
        );
    }

    private String nameOf(RouteRecommendationResponse.PlaceInfo place) {
        return place != null ? place.getName() : null;
    }

    private String foodAt(List<String> foods, int index) {
        return (foods != null && foods.size() > index) ? foods.get(index) : null;
    }
}
