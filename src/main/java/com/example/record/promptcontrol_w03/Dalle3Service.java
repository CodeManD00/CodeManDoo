// 패키지 선언. 이 클래스가 속한 폴더 경로와 유사한 개념이다.
package com.example.record.promptcontrol_w03;

// JSON 데이터를 다루기 위한 Jackson 라이브러리의 클래스들을 import.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Spring에서 application.properties 등으로부터 값을 읽어오기 위해 사용하는 어노테이션.
import org.springframework.beans.factory.annotation.Value;
// 이 클래스를 서비스(로직 담당 클래스)로 등록하기 위한 Spring 어노테이션.
import org.springframework.stereotype.Service;
// HTTP 요청을 보내기 위한 Spring의 비동기 클라이언트.
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map; // key-value 형태의 데이터를 담는 Map을 사용하기 위해 import.

/**
 * 이 클래스는 OpenAI의 DALL·E 3 API를 사용해서 이미지를 생성하고,
 * 그 결과로 나온 이미지 URL을 반환하는 기능을 담당한다.
 */
@Service // 이 클래스를 Spring이 관리하는 서비스로 등록한다.
public class Dalle3Service {

    // WebClient: 외부 API 요청을 보내기 위한 HTTP 클라이언트
    private final WebClient webClient;
    // ObjectMapper: JSON 데이터를 객체로 변환하거나, 그 반대로 변환할 수 있도록 도와주는 도구
    private final ObjectMapper objectMapper;
    // API 키: OpenAI API를 사용하기 위해 필요한 인증 키
    private final String apiKey;

    /**
     * 생성자: 이 클래스가 생성될 때 실행됨.
     * application.properties에서 openai.api.key 값을 가져와서 API 키로 설정함.
     * 동시에 WebClient를 설정해서 baseUrl, Authorization 헤더, Content-Type 설정을 마친다.
     */
    public Dalle3Service(@Value("${openai.api.key}") String apiKey) {
        this.apiKey = apiKey; // 가져온 API 키를 필드에 저장
        this.webClient = WebClient.builder() // WebClient 객체 생성
                .baseUrl("https://api.openai.com/v1") // OpenAI API의 기본 주소
                .defaultHeader("Authorization", "Bearer " + apiKey) // 인증을 위한 헤더 설정
                .defaultHeader("Content-Type", "application/json") // 보낼 데이터가 JSON 형식임을 알림
                .build(); // 설정 완료 후 WebClient 인스턴스 생성
        this.objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 생성
    }

    /**
     * 이 메서드는 입력된 프롬프트(prompt)를 DALL·E 3 API에 보내고,
     * 생성된 이미지의 URL만 추출해서 반환하는 기능을 한다.
     *
     * @param prompt 사용자 또는 시스템이 제공하는 이미지 설명 프롬프트
     * @return 생성된 이미지의 URL 문자열 (에러 발생 시 에러 메시지 문자열 반환)
     */
    public String generateImageUrlOnly(String prompt) {
        try {
            // webClient를 사용하여 OpenAI의 /images/generations API로 POST 요청을 보냄.
            String response = webClient.post()
                    .uri("/images/generations") // API의 엔드포인트 경로
                    .bodyValue(Map.of( // 요청 본문(body)을 Map으로 구성함 (JSON으로 자동 변환됨)
                            "model", "dall-e-3", // 사용할 이미지 생성 모델
                            "prompt", prompt,     // 생성에 사용할 프롬프트 입력
                            "n", 1,               // 이미지 개수: 1개만 요청
                            "size", "1024x1024"   // 이미지 해상도 설정 (정사각형)
                    ))
                    .retrieve() // 응답 받기 시작
                    .bodyToMono(String.class) // 응답 본문을 String으로 받음 (비동기)
                    .block(); // block()을 사용하여 결과가 올 때까지 기다림 (동기 방식으로 전환)

            // 응답으로 받은 JSON 문자열을 파싱해서 JsonNode 객체로 변환
            JsonNode root = objectMapper.readTree(response);
            // JSON 구조에서 data[0].url 값을 찾아 반환함 (첫 번째 이미지의 URL)
            return root.get("data").get(0).get("url").asText();

        } catch (Exception e) {
            // 에러 발생 시 콘솔에 에러 내용을 출력하고, 에러 메시지를 반환함.
            e.printStackTrace();
            return "Error generating image URL";
        }
    }
}
