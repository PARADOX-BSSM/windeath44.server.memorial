# Memorial Bow Status PRD

| Version | Date       | Author   | Branch                 |
|---------|------------|----------|------------------------|
| 0.1     | 2025-11-18 | Codex AI | `docs-0.3.16/docs` |

## 1. Overview
- **Background:** Memorial visitors can bow once every 24 hours per memorial. Users currently receive an error only after they attempt to bow twice. We need a dedicated status endpoint (GET `/memorials/bow/status/{userId}/{memorialId}`) for clients to query eligibility and display the next available bowing slot proactively.
- **Objective:** Provide a reliable, low-latency API that returns `canBow` and an `availableAt` timestamp formatted as `yyyy-MM-dd HH:mm:ss` (KST) so clients can gate UI interactions and surface countdown messaging.

## 2. Goals & Non-Goals
### Goals
1. Surface a definitive "bow eligibility" status for any (user, memorial) combination.
2. Standardize time formatting and timezone handling across clients.
3. Deliver deterministic behavior tied to the existing 24-hour cool-down logic in `MemorialBowService`.
4. Ensure latency < 150ms at P95 for 95% of requests under expected load (2k rps burst, 500 rps sustained).
5. Return stable, cache-friendly responses that update immediately after a bow.

### Non-Goals
- Altering the existing bow cool-down duration (remains 24 hours).
- Modifying Kafka events or downstream analytics triggered when a bow actually occurs.
- Handling localization beyond timestamp formatting (clients translate text).
- Implementing client-side countdown timers (only API data contract defined here).

## 3. User Personas & Stories
1. **Mourner / Visitor:** "As a mourner, I want to know when I can bow next so that I can plan without triggering an error." Acceptance: Mobile client displays remaining time; action button disabled until `canBow=true`.
2. **Caretaker App Admin:** "As an admin, I need to confirm when a high-volume user is rate-limited." Acceptance: Admin console uses the same endpoint for support workflows.
3. **Backend Integrations:** "As an automation service, I must check eligibility before scheduling ceremonial actions." Acceptance: Service consumes the endpoint and logs scheduling decisions.

## 4. Functional Requirements
1. **Endpoint:** `GET /memorials/bow/status/{userId}/{memorialId}` accessible behind memorial-service API gateway. Requires `user-id` header for downstream auditing (even though `userId` path param exists) to maintain parity with other endpoints.
2. **Parameters:**
   - `userId` (path, string) – unique visitor id.
   - `memorialId` (path, long) – memorial primary key.
   - Optional `Accept-Language` header for future localization (ignored for now but must not break behavior).
3. **Response Payload:**
   ```json
   {
     "message": "Bow status for memorial id: {memorialId} and user id: {userId}",
     "data": {
       "canBow": true,
       "availableAt": "2025-11-18 19:30:00"
     }
   }
   ```
   - `availableAt` uses server local timezone (KST) with zero-padded date/time.
   - When `canBow=true`, `availableAt` shows the current server time (acts as "available immediately").
4. **Business Rules:**
   - If no prior record exists, assume `bowCount=0`, `canBow=true`, `availableAt=now`.
   - After a bow, `lastBowedAt` is updated; eligibility opens exactly 24 hours later (inclusive). Example: bow at `10:15:30`, next available `>= 10:15:30` next day.
   - If `lastBowedAt + 24h` equals now, treat as `canBow=true`.
5. **Error Handling:**
   - `404` if memorial does not exist (`MemorialNotFoundException`).
   - `500` for unexpected errors, following global exception handler.
   - Response should still conform to `ResponseDto` envelope.
6. **Observability:**
   - Log at INFO: `bow-status-check user={userId} memorial={memorialId} canBow={canBow}`.
   - Emit Prometheus metrics: `bow_status_requests_total`, `bow_status_latency_seconds`, `bow_status_can_bow_ratio`.
7. **Throughput & Scaling:**
   - Endpoint reads from primary DB (MySQL). Add compound index `(user_id, memorial_id)` on `memorial_bow` table if not already present to keep query under 20ms.
   - Endpoint is cacheable with TTL <= remaining cool-down, but initial release will rely on DB lookups. Consider CDN caching for admin portal later.

## 5. Detailed Flow
1. Client calls endpoint every time the bow CTA screen loads or via background polling (no more often than every 10 seconds per spec to prevent abuse).
2. Service validates memorial existence -> fetches `MemorialBow` record -> computes `availableAt` -> returns `canBow` boolean and timestamp string.
3. When a user bows:
   - `MemorialBowService` increments `bowCount`, updates `lastBowedAt` to `LocalDateTime.now()`.
   - Subsequent status checks within 24 hours read updated `lastBowedAt` and respond with `canBow=false`, `availableAt` future time.
4. After 24 hours, the same endpoint flips `canBow` automatically without needing further writes.

## 6. Data Model & Contracts
- **MemorialBow** table fields consumed: `id`, `user_id`, `memorial_id`, `bow_count`, `last_bowed_at`.
- New DTO: `MemorialBowStatusResponseDto(canBow:boolean, availableAt:string)`.
- Response envelope remains `ResponseDto<String, T>`.
- Ensure `lastBowedAt` is always populated upon record creation (default `now`).

## 7. Dependencies & Integration
- No new message bus events. Kafka producer unaffected.
- Requires application layer access to `MemorialBowRepository` and `MemorialRepository` (already available).
- Time formatting uses `DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")`; ensure timezone config uses server default (KST) by running JVM with `-Duser.timezone=Asia/Seoul` or explicitly converting via `ZoneId`.

## 8. Security & Privacy
- Endpoint requires existing auth headers (handled by API gateway). Service trusts `user-id` header.
- No PII beyond user ID and memorial ID is exposed.
- Add rate limiting at gateway: 60 req/min per user to prevent brute-force enumeration.

## 9. Analytics & Telemetry
- Track: total requests, unique users, distribution of `canBow` responses, average cooldown remaining when `canBow=false`.
- Feed metrics to BI dashboards to observe peak bow windows (e.g., 10AM/8PM) for capacity planning.

## 10. Rollout Plan
1. **Dev**: Implement endpoint + tests in feature branch `fix-0.3.15/bowStatus/PW-329` (already complete).
2. **Docs**: Merge this PRD into `docs-0.3.16/docs` branch, link from project wiki.
3. **QA**: Add Postman/Newman regression script verifying formatting and boundary conditions (23h59m, 24h, brand-new user).
4. **Release**: Deploy memorial-service v0.3.16 to staging, run load test (1k rps). If stable for 24h, promote to production via blue/green.
5. **Monitoring**: Add Grafana alert when `bow_status_latency_seconds_p95 > 250ms` for 5 minutes.

## 11. Risks & Mitigations
| Risk | Impact | Mitigation |
|------|--------|------------|
| High read load on memorial_bow table | Elevated DB CPU | Add covering index & read replicas, consider caching in Redis keyed by `(userId:memorialId)` with TTL. |
| Timezone drift between servers | Users see inconsistent countdowns | Enforce timezone configuration at JVM level and add contract tests for formatting. |
| Clients poll too frequently | API throttling, cost | Communicate recommended polling interval, enforce rate limiting. |
| Missing data for deleted memorials | 404 loops in client | Provide UI copy for deleted memorials; add analytics to track frequency. |

## 12. Open Questions
1. Should we expose `remainingSeconds` additionally to simplify client countdown rendering?
2. Do we need localization-ready timestamp (e.g., ISO 8601 + timezone offset) for future multi-region support?
3. Should anonymous visitors (no `user-id`) be able to retrieve bow status for read-only preview states?
4. Should admin/support endpoints bypass rate limiting for investigations?
