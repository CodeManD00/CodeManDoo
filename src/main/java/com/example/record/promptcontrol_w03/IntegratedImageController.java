// 현재 클래스가 속한 패키지를 선언
package com.example.record.promptcontrol_w03;

// 필요한 클래스들을 import (데이터 전달용 DTO, HTTP 응답 관련 등)
import com.example.record.promptcontrol_w03.dto.ImageResponse;
import com.example.record.promptcontrol_w03.dto.PromptRequest;
import com.example.record.promptcontrol_w03.dto.PromptResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 이 클래스가 REST API의 컨트롤러임을 Spring에 알림 (@Controller + @ResponseBody 의미)
@RestController
// 이 컨트롤러의 기본 경로를 설정. "/generate-image"로 시작하는 요청을 이 컨트롤러가 처리함.
@RequestMapping("/generate-image")
public class IntegratedImageController {

    // 프롬프트를 생성하는 로직을 담당하는 서비스
    private final PromptService promptService;
    // 이미지를 생성하고 URL을 반환하는 로직을 담당하는 서비스
    private final Dalle3Service dalle3Service;

    /**
     * 생성자 주입 방식으로 두 서비스를 외부에서 받아서 사용함.
     * 스프링이 자동으로 객체를 주입해줌 (DI).
     */
    public IntegratedImageController(PromptService promptService, Dalle3Service dalle3Service) {
        this.promptService = promptService;
        this.dalle3Service = dalle3Service;
    }

    /**
     * HTTP POST 요청을 처리하는 메서드.
     * 사용자가 "/generate-image"로 POST 요청을 보내면 이 메서드가 실행됨.
     * @param request 사용자로부터 전달된 프롬프트 입력값(JSON → PromptRequest 객체로 자동 변환됨)
     * @return 생성된 프롬프트와 이미지 URL이 포함된 응답 객체 (ImageResponse 형태)
     */
    @PostMapping
    public ResponseEntity<ImageResponse> generateImage(@RequestBody PromptRequest request) {
        // 먼저 사용자의 요청을 기반으로 프롬프트 문장을 생성함
        PromptResponse promptResponse = promptService.generatePrompt(request);
        // 생성된 프롬프트 문장만 꺼냄
        String prompt = promptResponse.getPrompt();

        // DALL·E 3 API를 사용해서 해당 프롬프트에 맞는 이미지를 생성하고, 이미지 URL만 받아옴
        String imageUrl = dalle3Service.generateImageUrlOnly(prompt);

        // 최종 응답 객체를 만들어서, 생성된 프롬프트와 이미지 URL을 담음
        ImageResponse response = new ImageResponse();
        response.setPrompt(prompt);      // 프롬프트 문장
        response.setImageUrl(imageUrl);  // 이미지 URL

        // HTTP 상태 200(성공)과 함께 응답을 반환
        return ResponseEntity.ok(response);
    }
}
