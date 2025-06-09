// 이 클래스는 com.example.record 패키지에 속함을 나타냄
package com.example.record;

// JPA(자바 퍼시스턴스 API) 관련 애노테이션과 타입들을 가져옴
import jakarta.persistence.*;

// 날짜와 시간을 저장하기 위한 Java 클래스
import java.time.LocalDateTime;

// 이 클래스가 데이터베이스 테이블과 매핑되는 JPA 엔티티임을 나타냄
@Entity
public class ApiKey {

    // 이 필드는 테이블의 기본 키(PK)임을 나타냄
    @Id
    // ID를 자동으로 증가시켜주는 전략 (자동 번호 부여)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 식별자

    private String userId;  // API 키를 등록한 사용자 ID
    private String apiKey;  // 실제 사용되는 OpenAI API 키

    // 객체가 생성되는 순간의 시간을 자동으로 저장
    private LocalDateTime createdAt = LocalDateTime.now();

    // JPA가 객체를 생성할 때 기본 생성자가 필요하므로 명시
    public ApiKey() {}

    // 사용자 ID와 API 키를 받아 초기화하는 생성자
    public ApiKey(String userId, String apiKey) {
        this.userId = userId;
        this.apiKey = apiKey;
    }

    // 아래는 getter/setter 메서드들

    // 고유 ID를 반환 (읽기 전용, setter 없음)
    public Long getId() {
        return id;
    }

    // 사용자 ID 값을 반환
    public String getUserId() {
        return userId;
    }

    // 사용자 ID 값을 설정
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // API 키 값을 반환
    public String getApiKey() {
        return apiKey;
    }

    // API 키 값을 설정
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    // 생성 시각을 반환
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // 생성 시각을 수동으로 설정할 수도 있게 함 (보통은 잘 안 씀)
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
