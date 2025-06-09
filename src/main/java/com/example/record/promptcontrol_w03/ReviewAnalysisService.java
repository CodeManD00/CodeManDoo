// 이 클래스는 공연 후기를 GPT-4 API에 보내 분석하고,
// 그 결과로 프롬프트 생성을 위한 핵심 요소들을 JSON 형태로 반환합니다.
package com.example.record.promptcontrol_w03;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service // Spring이 이 클래스를 서비스로 인식해서 빈으로 등록하게 해주는 어노테이션입니다.
public class ReviewAnalysisService {

    // application.properties에 설정된 OpenAI API 키를 주입받습니다.
    private final WebClient webClient;

    public ReviewAnalysisService(@Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    // 사용자의 후기(reviewText)를 받아 GPT-4에 전달하고 분석 결과를 반환하는 메서드입니다.
    public Map<String, Object> analyzeReview(String reviewText) {
        // GPT에게 보낼 프롬프트 문장을 작성합니다.
        // 사용자의 후기 내용을 포함한 형식으로 구성합니다.
        String prompt = String.format("""
        다음 공연 후기를 분석하여 다음 항목을 JSON 형식으로 추출해줘.
        - emotion
        - theme
        - setting
        - relationship
        - actions
        - character1
        - character2
        - (가능하면 character3, character4 도 포함)
        - lighting

            후기: %s
        """, reviewText);

        // OpenAI API에 보낼 요청 본문(JSON)을 구성합니다.
        Map<String, Object> body = Map.of(
                // 사용할 모델은 gpt-4입니다.
                "model", "gpt-4",
                // messages는 ChatGPT 포맷으로, 시스템 메시지 없이 유저 메시지만 넣습니다.
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                // temperature는 창의성(랜덤성)을 조절하는 값으로, 0.7은 중간 정도의 무작위성을 의미합니다.
                "temperature", 0.7
        );

        // WebClient를 이용하여 GPT-4에게 POST 요청을 보냅니다.
        String response = webClient.post()
                // API 경로: /chat/completions (ChatGPT 방식 요청)
                .uri("/chat/completions")
                // 요청 본문(body) 설정
                .bodyValue(body)
                // 응답을 String 형태로 받아옵니다.
                .retrieve()
                .bodyToMono(String.class)
                .block(); // block()은 비동기 결과를 동기로 기다리며, 여기선 단순히 동기적으로 처리

        // JSON 응답을 파싱하기 위한 Jackson ObjectMapper 객체를 생성합니다.
        ObjectMapper mapper = new ObjectMapper();

        // GPT 응답에서 우리가 필요한 실제 텍스트(내용)를 추출합니다.
        // "/choices/0/message/content" 경로에 생성된 텍스트가 들어있습니다.
        try {
            JsonNode json = mapper.readTree(response);
            String content = json.at("/choices/0/message/content").asText();
            return mapper.readValue(content, new TypeReference<>() {}); // 그 텍스트는 우리가 기대한 JSON 형식이므로, 다시 JSON으로 파싱해서 Map으로 반환합니다.
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Map.of("error", "JSON 파싱 실패");
        }
    }
}

