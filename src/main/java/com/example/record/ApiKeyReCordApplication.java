// 프로젝트의 루트 패키지로 설정된 com.example.record 패키지에 속함
package com.example.record;

import jakarta.annotation.PostConstruct; // 메서드 실행 시점을 제어하는 어노테이션
import org.springframework.beans.factory.annotation.Value; // application.properties 값 주입용 어노테이션
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 이 애플리케이션이 Spring Boot 애플리케이션임을 명시함 (자동 설정 + 컴포넌트 스캔)
@SpringBootApplication
public class ApiKeyReCordApplication {

    // ① application.properties 파일에서 설정한 openai.api.key 값을 주입받음
    //    → 예: openai.api.key=sk-xxxxxxxxxxx
    @Value("${openai.api.key}")
    private String apiKey;

    // ② 프로그램의 진입점 (main 메서드)
    //    SpringApplication.run()을 호출하면 내장 서버가 뜨고 앱이 실행됨
    public static void main(String[] args) {
        SpringApplication.run(ApiKeyReCordApplication.class, args);
    }

    // ③ 애플리케이션 실행 직후 실행되는 메서드
    //    → @PostConstruct는 객체 생성 + 의존성 주입이 끝난 후 호출됨
    @PostConstruct
    public void checkApiKey() {
        // 콘솔에 API 키가 잘 주입되었는지 확인용 출력
        System.out.println("✅ Loaded API Key: " + apiKey);
    }
}
