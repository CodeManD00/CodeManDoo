// 이 클래스는 com.example.record 패키지에 속함
package com.example.record;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 이 클래스가 Spring이 관리하는 Bean임을 명시함 (자동으로 등록됨)
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    // API 키가 유효한지 확인할 때 사용할 Repository (데이터베이스 접근용)
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    // 모든 요청이 컨트롤러에 도달하기 전에 먼저 이 메서드가 실행됨
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // HTTP 요청 헤더에서 Authorization 값을 가져옴
        String header = request.getHeader("Authorization");
        System.out.println("=== Authorization 헤더: " + header);  // 디버깅용 로그 출력

        // 헤더가 없거나 Bearer 토큰 형식이 아닌 경우
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println(">>> Authorization 헤더 누락 또는 잘못된 형식");

            try {
                // 401 Unauthorized 에러를 반환하고 요청 차단
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header");
            } catch (IOException e) {
                e.printStackTrace();  // 에러 로그 출력
            }
            return false; // 컨트롤러로 요청이 가지 않도록 막음
        }

        // "Bearer " 다음의 문자열을 잘라내어 실제 API 키 추출
        String apiKey = header.substring(7);
        System.out.println("=== 추출된 API 키: " + apiKey);  // 디버깅용 로그 출력

        // 데이터베이스에 해당 API 키가 존재하는지 확인
        boolean exists = apiKeyRepository.existsByApiKey(apiKey);
        System.out.println("=== DB에 키 존재 여부: " + exists);

        // 존재하지 않으면 401 Unauthorized 응답
        if (!exists) {
            System.out.println(">>> 존재하지 않는 API 키");
            try {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API Key");
            } catch (IOException e) {
                e.printStackTrace();  // 에러 로그 출력
            }
            return false;
        }

        // 여기까지 통과하면 요청을 정상적으로 컨트롤러로 전달
        return true;
    }

}
