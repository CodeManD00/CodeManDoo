// com.example.record 패키지에 위치한 인터페이스
package com.example.record;

import org.springframework.data.jpa.repository.JpaRepository;

// ApiKeyRepository는 JPA를 사용해 DB와 자동으로 연결해주는 인터페이스입니다.
// JpaRepository<엔티티 타입, 기본키 타입>을 상속받으면, 복잡한 쿼리 없이도 CRUD 작업이 자동으로 제공됩니다.
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    // 이 메서드는 "apiKey"라는 값을 기준으로 DB에 존재하는지 확인하는 기능을 제공합니다.
    // 메서드 이름만 이렇게 지으면, JPA가 자동으로 쿼리를 만들어줍니다.
    // → 실제 SQL 쿼리 없이도 "SELECT EXISTS ... WHERE api_key = ?" 처럼 작동함
    boolean existsByApiKey(String apiKey);
}
