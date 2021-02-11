# Spring Web Kotlin Blocking

## 공유할 것

### JPA

https://spring.io/guides/tutorials/spring-boot-kotlin/#_persistence_with_jpa

`allopen` 플러그인 사용법

## 결론

### 장점

1. repository에서 Optional monad 대신 일반 객체 return 가능. not found인 경우 `EmptyResultDataAccessException` 이 발생한다. ControllerAdvice에서 이 경우에 404를 내보내도록 하면 도메인별로 매번 새로운 exception을 만들지 않아도 됨.
    - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.core-concepts