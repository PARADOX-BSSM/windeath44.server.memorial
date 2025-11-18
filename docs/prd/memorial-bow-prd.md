# Memorial Bow Experience PRD

| Version | Date       | Author   | Scope                               |
|---------|------------|----------|--------------------------------------|
| 0.2     | 2025-11-18 | Codex AI | Bow creation + counts + status APIs |

## 1. Overview
- **Background:** Bowing is the central action visitors take to honor a memorial. Each user may bow to a specific memorial once every 24 hours. Existing UX only revealed cooldowns after failure; now we expose a read endpoint in addition to write/count operations.
- **Objectives:**
  1. Provide a dependable POST `/memorials/bow` action that enforces cooldown, publishes Kafka events, and increments memorial bow counts.
  2. Surface aggregate counts via `GET /memorials/bow/{memorialId}`.
  3. Offer predictive status via `GET /memorials/bow/status/{userId}/{memorialId}` so clients can preemptively gate CTAs.
- **KPIs:** Maintain <1% failure rate (non-business) and <150 ms P95 latency on read endpoints.

## 2. User Stories
1. **Visitor:** “I want to pay respects once per day without encountering errors.”
2. **Client App:** “I need to disable the bow button when a user is in cooldown and show when they can bow again.”
3. **Data Platform:** “We must broadcast bow events to Kafka for downstream analytics.”

## 3. API Matrix
| Endpoint | Method | Description | Auth | Response |
|----------|--------|-------------|------|----------|
| `/memorials/bow` | POST | Perform bow for authenticated user. | Requires `user-id` header. | `ResponseDto<Void>` success message; 201 status. Throws 403 with remain time if within cooldown.
| `/memorials/bow/{memorialId}` | GET | Retrieve total bow count for memorial. | Public. | `ResponseDto<Long>` returning total bow count.
| `/memorials/bow/status/{userId}/{memorialId}` | GET | Determine if user can bow now and return formatted timestamp. | Public but typically same user; optional header `user-id` for tracing. | `ResponseDto<MemorialBowStatusResponseDto>`.
| `/memorials/bow/{userId}/{memorialId}` | GET | Debug endpoint returning raw entity info. | Public; used internally. | `ResponseDto<MemorialBowResponseDto>`.

## 4. Business Logic
- **Cooldown Enforcement:** `MemorialBow.lastBowedAt` tracked per (user, memorial). Service rejects bow attempts if `lastBowedAt > now - 24h`, returning formatted `remainTime` in exception message.
- **State Mutation:**
  - `MemorialBow` record created on first bow with `bowCount=1`, `lastBowedAt=now`.
  - Subsequent bows increment `bowCount`, update `lastBowedAt` to `now`.
  - Parent memorial’s `bowCount` increments regardless of user.
- **Eventing:** Successful bows emit `MemorialBowedAvroSchema(memorialId, memorialBowCount, userId)` to Kafka topic `memorial-bowed-request`.
- **Status Formatting:** `availableAt` returned as `yyyy-MM-dd HH:mm:ss` (KST). When `canBow=true`, timestamp equals `now`. Otherwise timestamp equals `lastBowedAt + 24h`.
- **Counting:** `/bow/{memorialId}` performs DB aggregation `sum(bowCount)` and defaults to `0` when no rows exist.

## 5. Error Handling
| Scenario | Response | Notes |
|----------|----------|-------|
| Memorial not found | 404 `MemorialNotFoundException` | Shared handler.
| Bow within cooldown | 403 `BowedWithin24HoursException` + `remainTime` string `HH:mm:ss`. | Called before persistence.
| Invalid memorialId format | 400 (Spring). |

## 6. Data Contracts
- **MemorialBowStatusResponseDto**: `{ "canBow": boolean, "availableAt": "yyyy-MM-dd HH:mm:ss" }`.
- **MemorialBowResponseDto**: raw entity fields (IDs, counts, `lastBowedAt`).
- **Exception payload**: `ResponseDto` envelope with error message and optional data.

## 7. Observability
- Log at INFO for each status check and bow action: `bow-action event=<status|write> user=<id> memorial=<id> canBow=<bool> latencyMs=<...>`.
- Metrics:
  - `bow_requests_total` (POST) with labels `result=success|cooldown|error`.
  - `bow_status_checks_total` and `bow_status_latency_seconds`.
  - `bow_count_reads_total`.

## 8. Performance & Scaling
- Add composite index `(user_id, memorial_id)` on `memorial_bow`.
- Use DB-level row locking during bow updates to avoid lost updates (JPA handles via transactional boundaries).
- Status endpoint is read-heavy; consider caching `(user, memorial)` response with TTL = cooldown remaining.

## 9. Security & Abuse Prevention
- Enforce gateway auth for POST (user-id token). Resist spoofing by verifying header signature (future enhancement).
- Rate limit status endpoint to 120 req/min per user to prevent enumeration.
- Kafka topic requires ACL to memorial service only.

## 10. Risks & Mitigations
| Risk | Impact | Mitigation |
|------|--------|------------|
| Clock skew between instances | Wrong cooldown windows | Synchronize time via NTP; optionally store actions using DB server time. |
| Rapid repeated requests | Spike in DB load | Throttle at gateway and add in-memory guard (Caffeine). |
| Kafka outage | Events lost | Use retry/backoff; log failures. |

## 11. Rollout Checklist
1. Implement service logic + DTOs (complete in `fix-0.3.15/bowStatus/PW-329`).
2. QA scenario table covering first bow, repeated bow, boundary at 24h, aggregated count zero, and handling missing memorial.
3. Monitor metrics post-release and watch for 403 spikes.
