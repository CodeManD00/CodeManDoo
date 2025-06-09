// 이 클래스는 프론트엔드에서 백엔드로 "프롬프트 생성 요청"을 보낼 때 사용하는 데이터 구조입니다.
// 사용자가 입력한 공연 정보(제목, 장소, 날짜, 장르, 출연진, 후기)를 담습니다.

package com.example.record.promptcontrol_w03.dto;

import java.util.List;

public class PromptRequest {

    // 공연 제목 (예: "웃는 남자")
    private String title;

    // 공연 장소 (예: "예술의 전당 오페라극장")
    private String location;

    // 공연 날짜 (예: "2025-06-10")
    private String date;

    // 장르 (예: "뮤지컬", "밴드") — 이 값을 바탕으로 어떤 프롬프트 템플릿을 쓸지 결정
    private String genre;

    // 출연진 목록 (예: ["홍길동", "김연우"]) — 나중에 확장 가능
    private List<String> cast;

    // 사용자가 입력한 후기 — GPT 분석을 위한 핵심 입력값
    private String review;

    // ✅ 아래는 getter 메서드들입니다.
    // getter는 외부에서 이 클래스의 정보를 읽을 수 있게 도와줍니다.

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getGenre() {
        return genre;
    }

    public List<String> getCast() {
        return cast;
    }

    public String getReview() {
        return review;
    }

    // ❗ setter 메서드도 함께 정의되어 있습니다.
    // 외부에서 이 객체를 만들고 값을 채울 때 사용됩니다.

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
