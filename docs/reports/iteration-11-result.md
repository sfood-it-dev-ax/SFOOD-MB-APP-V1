# Iteration 11 Result

## Delivered
- `googleToken` 저장 기능 추가
- 동일 사용자 재로그인 시 저장된 `googleToken`과 동일성 검증 추가
- 토큰 불일치 시 `401 UNAUTHORIZED` 반환

## Changed Files (Core)
- DB migration:
  - `V3__create_google_tokens_table.sql`
- Domain/Repository:
  - `GoogleToken` entity
  - `GoogleTokenRepository` + JPA/InMemory adapters
- Service:
  - `AuthServiceImpl` login flow:
    - first login: token 저장
    - next login: 동일성 검증

## Validation
- `./gradlew test` PASS
- `./gradlew build` PASS

## Over-scope Check
- 요청 범위 외 기능(roles/admin/JWT/추가 API) 변경 없음.
