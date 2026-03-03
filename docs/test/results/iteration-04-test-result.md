# Iteration 04 Test Result

## Configuration Applied
- File: `src/main/resources/application-postgres.properties`
- Added options:
  - `spring.jpa.properties.hibernate.default_schema=${DB_SCHEMA:memo_board_v3}`
  - `spring.jpa.properties.hibernate.hbm2ddl.create_namespaces=true`
- Changed default postgres URL to database-level URL:
  - `jdbc:postgresql://localhost:5432/postgres`

## Live Execution
1. Boot app with new schema
- Command:
  - `SPRING_PROFILES_ACTIVE=postgres`
  - `DB_URL=jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require`
  - `DB_SCHEMA=memo_board_v4_20260227180730`
  - `./gradlew bootRun`
- Result: PASS

2. Full API live test
- Log: `docs/test/results/iteration-04-live-api-log.md`
- Result: PASS

3. Schema/table verification query
- Log: `docs/test/results/iteration-04-db-schema-check.md`
- Result: PASS

4. Regression test
- Command: `./gradlew test`
- Result: PASS

## Final Verdict
- Schema creation: PASS
- Table creation: PASS
- Full API retest: PASS
