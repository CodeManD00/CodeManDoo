// 프로젝트에서 전반적인 웹 설정을 담당하는 클래스임을 나타냄
package com.example.record;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

// 이 클래스는 Spring Boot에서 제공하는 웹 설정 클래스입니다.
// WebMvcConfigurer를 상속해서 원하는 설정들을 커스터마이징할 수 있게 해줍니다.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ApiKeyInterceptor라는 클래스의 인스턴스를 자동으로 주입받습니다.
    // → 이 Interceptor는 API 요청이 들어올 때마다 API 키가 유효한지 확인하는 역할을 함
    @Autowired
    ApiKeyInterceptor apiKeyInterceptor;

    // 이 메서드는 우리가 만든 Interceptor를 실제로 Spring MVC에 등록하는 부분입니다.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor를 등록하고, 어느 URL 경로에 적용할지를 설정합니다.
        // 여기서는 "/api/image/**" 경로로 시작하는 모든 요청에 대해서 Interceptor를 작동시킵니다.
        registry.addInterceptor(apiKeyInterceptor).addPathPatterns("/api/image/**");

        // 예) "/api/image/generate" 같은 API 요청이 들어오면,
        //     먼저 ApiKeyInterceptor가 API 키를 검사하고 통과하면 컨트롤러로 전달됨
    }
}
