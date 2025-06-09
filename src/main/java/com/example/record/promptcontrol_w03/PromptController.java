// 현재 클래스가 포함된 패키지를 명시
package com.example.record.promptcontrol_w03;

// 사용자 요청과 응답에 사용될 DTO 클래스들을 import
import com.example.record.promptcontrol_w03.dto.PromptRequest;
import com.example.record.promptcontrol_w03.dto.PromptResponse;

// Spring 웹 프레임워크에서 사용하는 주요 클래스들 import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 이 클래스가 REST API용 컨트롤러임을 선언 (@Controller + @ResponseBody 역할)
@RestController
// 이 컨트롤러가 처리할 기본 URL 경로를 "/prompt"로 설정
@RequestMapping("/prompt")
public class PromptController {

    // 프롬프트 생성을 담당하는 서비스 객체
    private final PromptService promptService;

    /**
     * 생성자를 통해 promptService를 주입 받음.
     * Spring이 자동으로 PromptService 객체를 생성해 넣어줌(DI, 의존성 주입).
     */
    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    /**
     * POST 요청을 처리하는 메서드.
     * 클라이언트가 "/prompt"로 POST 요청을 보내면 실행됨.
     *
     * @param request 사용자로부터 받은 프롬프트 입력값 (JSON → PromptRequest 객체로 자동 변환됨)
     * @return 생성된 프롬프트를 담은 응답(PromptResponse)을 HTTP 200(OK) 상태로 반환
     */
    @PostMapping
    public ResponseEntity<PromptResponse> generatePrompt(@RequestBody PromptRequest request) {
        // PromptService를 통해 입력값 기반의 프롬프트 생성 로직 실행
        PromptResponse response = promptService.generatePrompt(request);

        // 생성된 프롬프트를 HTTP 응답 형식으로 반환 (JSON 형태로 클라이언트에 전달됨)
        return ResponseEntity.ok(response);
    }
}
