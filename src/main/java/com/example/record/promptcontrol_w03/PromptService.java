// 현재 클래스가 속한 패키지를 명시
package com.example.record.promptcontrol_w03;

// 프롬프트 요청/응답을 위한 DTO 클래스들 import
import com.example.record.promptcontrol_w03.dto.PromptRequest;
import com.example.record.promptcontrol_w03.dto.PromptResponse;

// 이 클래스가 서비스 역할을 수행함을 나타냄. 스프링이 자동으로 빈으로 등록함
import org.springframework.stereotype.Service;

// 리뷰 분석을 담당하는 서비스 클래스 import
import com.example.record.promptcontrol_w03.ReviewAnalysisService;

import java.util.HashMap;
import java.util.Map;

// 실제 프롬프트 문장을 만들어주는 서비스 클래스
@Service
public class PromptService {

    // 후기 분석을 담당하는 서비스 클래스의 참조
    private final ReviewAnalysisService reviewAnalysisService;

    // 생성자를 통해 ReviewAnalysisService 주입
    public PromptService(ReviewAnalysisService reviewAnalysisService) {
        this.reviewAnalysisService = reviewAnalysisService;
    }

    /**
     * 사용자의 요청(PromptRequest)을 바탕으로 프롬프트를 생성하는 메서드.
     * 장르(뮤지컬/밴드)에 따라 서로 다른 프롬프트를 생성함.
     */
    public PromptResponse generatePrompt(PromptRequest input) {
        // 사용자가 선택한 장르 (ex. "뮤지컬", "밴드")
        String genre = input.getGenre();
        String prompt;

        // 장르에 따라 다른 방식의 프롬프트 생성
        if ("뮤지컬".equals(genre)) {
            prompt = generateMusicalPrompt(input);
        } else if ("밴드".equals(genre)) {
            prompt = generateBandPrompt(input);
        } else {
            // 정의되지 않은 장르인 경우 예외 발생
            throw new IllegalArgumentException("지원하지 않는 장르입니다: " + genre);
        }

        // 생성된 프롬프트를 응답 객체(PromptResponse)에 담아 반환
        PromptResponse response = new PromptResponse();
        response.setPrompt(prompt);

        // 프롬프트 생성 결과에 포함될 부가 메타 정보 구성
        Map<String, Object> meta = new HashMap<>();
        meta.put("structure", genre); // 장르
        meta.put("style", "gothic");  // 스타일 (예시용 고정값)
        meta.put("tone", "emotional"); // 톤
        meta.put("inferred_keywords", new String[]{"obsession", "conflict"}); // 추론된 키워드 (현재 고정값)
        response.setMeta(meta);

        return response;
    }

    /**
     * 뮤지컬 장르에 대한 프롬프트 생성 메서드
     * 후기(review)를 GPT에 보내서 분석한 결과로 프롬프트를 구성함
     */
    private String generateMusicalPrompt(PromptRequest input) {
        // 후기 분석 결과를 Map 형태로 받아옴
        Map<String, Object> data = reviewAnalysisService.analyzeReview(input.getReview());

        // 등장 인물 텍스트 조합 (최소 2명, 최대 5명)
        String characterPart = String.format("%s and %s",
                data.get("character1"),
                data.get("character2")
        );
        // 추가 인물(3~5)이 있는 경우 이어붙이기
        for (int i = 3; i <= 5; i++) {
            String key = "character" + i;
            if (data.containsKey(key)) {
                characterPart += ", and " + data.get(key);
            }
        }

        // 최종 프롬프트 문자열 생성
        return String.format("""
        A %s musical theater scene about %s,
        set in %s and depicting %s,
        featuring %s,
        with %s,
        under %s.
        No captions, no letters, no text in the image.
        """,
                data.get("emotion"),     // 감정/분위기 형용사
                data.get("theme"),       // 주제
                data.get("setting"),     // 시대적 배경 및 장소
                data.get("relationship"),// 인물 관계 및 줄거리 요약
                data.get("actions"),     // 장면 요약
                characterPart,           // 인물 외형 및 감정
                data.get("lighting")     // 조명/연출 요소
        );
    }

    /**
     * 밴드 장르에 대한 프롬프트 생성 메서드
     * 아직 밴드는 후기 내용 분석 없이, 고정 포맷을 기반으로 구성됨
     */
    private String generateBandPrompt(PromptRequest input) {
        return String.format(
                "A moody alternative rock live performance scene by %s,\n" +
                        "featuring a powerful set with emotional lyrics,\n" +
                        "set during autumn,\n" +
                        "at %s on %s,\n" +
                        "with a stage design inspired by %s's concert visuals,\n" +
                        "including deep blue and purple lighting, fog machines and backlights,\n" +
                        "without showing any characters or text.\n" +
                        "No captions, no letters, no words in the image.",
                input.getTitle(), input.getLocation(), input.getDate(), input.getTitle()
        );
    }

    /**
     * (예정 기능) 후기 기반으로 공연 주제를 추출하는 메서드
     * 현재는 고정된 주제를 반환하고 있음.
     */
    private String extractTheme(String title, String review) {
        return "obsession and destructive love"; // TODO: 나중에 GPT나 위키 크롤링으로 대체 예정
    }
}
