// 이 클래스는 이미지 생성 결과를 클라이언트(예: 프론트엔드)에게 전달할 때 사용하는 데이터 구조입니다.
// → "프롬프트"와 "생성된 이미지 URL"이라는 두 가지 정보를 담고 있음
package com.example.record.promptcontrol_w03.dto;

public class ImageResponse {

    // 사용자가 입력한 프롬프트 문장을 저장할 변수
    private String prompt;

    // DALL·E API로 생성된 이미지의 URL을 저장할 변수
    private String imageUrl;

    // 🟢 setter 메서드: 외부에서 값을 설정할 수 있게 해줍니다.
    // 예: 컨트롤러나 서비스가 이 객체를 만들고 내부 데이터를 채울 때 사용

    // 프롬프트 값을 설정하는 메서드
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    // 이미지 URL 값을 설정하는 메서드
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 🟢 getter 메서드: 내부에 저장된 값을 외부로 꺼낼 수 있게 해줍니다.
    // 예: JSON 응답으로 보내기 위해 이 값을 읽게 됨

    // 저장된 프롬프트 값을 반환
    public String getPrompt() {
        return prompt;
    }

    // 저장된 이미지 URL 값을 반환
    public String getImageUrl() {
        return imageUrl;
    }
}
