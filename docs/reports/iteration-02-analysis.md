# Iteration 02 Analysis

## Objective
Transition repository layer from in-memory storage to JPA persistence without changing public API contracts.

## Change Scope
- Add JPA dependencies and datasource configuration.
- Convert core domain models into JPA-managed entities.
- Introduce Spring Data repository delegates.
- Add adapter implementations for existing repository interfaces.
- Keep in-memory implementations behind optional `memory` profile.
- Seed default `memo_types` records for API compatibility.

## Compatibility
- Controller and service interfaces remain unchanged.
- API payloads and response schema remain unchanged.

## Technical Decisions
- Use local H2 datasource for iteration validation.
- Use table names prefixed with `mb_` to avoid reserved-name conflicts in H2.
- Keep logical delete semantics with `isHide` flag.

## Risks
- Data is reset on application restart (H2 in-memory).
- Real PostgreSQL schema alignment is pending next iteration.
- OAuth real token verification remains deferred from Iteration 01.
