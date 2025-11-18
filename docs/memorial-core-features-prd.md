# Memorial Service Core Features PRD

| Version | Date       | Author   | Branch               |
|---------|------------|----------|----------------------|
| 0.1     | 2025-11-18 | Codex AI | `docs-0.3.16/docs` |

> **Scope**: All end-user and operator features of the memorial service **excluding** the `GET /memorials/bow/status/{userId}/{memorialId}` endpoint (documented separately). This PRD covers memorial discovery, contribution workflow, social features, chief rotation, anime catalog, and character management APIs.

## 1. Shared Principles
- **Auth & Headers**: Every write endpoint requires the `user-id` header; admin-only actions also require `role=ADMIN`.
- **Response Contract**: All controllers wrap payloads in `ResponseDto { message, data }` with localized message strings handled on clients.
- **Pagination**: Offset pagination (`OffsetPage`) uses fixed page size=10 unless specified. Cursor pagination (`CursorPage`) returns `values`, optional `nextCursorId`, and `hasNext`.
- **Error Handling**: Controller advice maps business exceptions (`MemorialNotFoundException`, `AuthenticationFailedException`, etc.) to structured HTTP errors.
- **Time Format**: All timestamps default to `LocalDateTime` serialized as ISO-8601; comments additionally force `yyyy-MM-dd` via `@JsonFormat`.
- **Tracing**: Requests including `user-id` emit `MemorialTracingEvent` for analytics.

## 2. Memorial Discovery & Filtering
### 2.1 Goals
1. Allow mourners to discover memorials via ID, bulk lookup, listing order, or character filters.
2. Provide deterministic ordering options for consistent infinite scroll experiences.

### 2.2 API Surface
| Endpoint | Method | Description | Notes |
|----------|--------|-------------|-------|
| `/memorials/{memorialId}` | GET | Fetch a single memorial plus metadata (chiefs, bow count, latest commit). | Optional `user-id` header triggers tracing.
| `/memorials/memorialIds?memorialIds=1,2,...` | GET | Batch lookup up to N memorials. | Returns list of `MemorialResponseDto` in request order.
| `/memorials?orderBy=recently-updated&page=0` | GET | Paginated listing ordered by `orderBy`. | Allowed orderBy: `recently-updated`, `lately-updated`, `ascending-bow-count`, `descending-bow-count`; invalid values -> 400.
| `/memorials/character-filtered` | POST | Paginated listing filtered by characters. | Request body: `{ "characters": [Long], "orderBy": "...", "page": 0 }`.

### 2.3 Business Rules
- Page size fixed at 10 to simplify caching; clients increment `page` sequentially.
- Character filter accepts up to 20 character IDs to bound SQL `IN` clause.
- `MemorialResponseDto` fields: `memorialId`, `characterId`, `chiefs`, `bowCount`, `memorialCommitId`, `content`, `userId`, `createdAt`, `mergerId`, `updatedAt`.
- Publishing `MemorialTracingEvent(memorialId, userId)` is mandatory when user context exists to support personalization analytics.

### 2.4 Metrics & SLAs
- P95 latency < 200 ms per lookup.
- `memorial_lookup_not_found_total` increments when ID queries miss.
- Monitor distribution of `orderBy` usage to inform caching strategy.

## 3. Memorial Contribution Workflow
### 3.1 Summary
Memorial content evolves via git-like workflow: commits -> pull requests -> review actions (merge, resolve conflicts, reject).

### 3.2 Entities
- **MemorialCommit**: user-generated content block tied to `memorialId`.
- **MemorialPullRequest**: wraps commit + target memorial, stores `state` (`PENDING`, `RESOLVED`, `APPROVED`, `REJECTED`, `STORED`).
- **Diff View**: textual diff string plus conflict flag for reviewer.

### 3.3 API Surface
| Endpoint | Method | Purpose | Payload |
|----------|--------|---------|---------|
| `/memorials/commit` | POST | Create commit draft. | `{ memorialId, content }`; returns commit ID.
| `/memorials/commits/{memorialId}` | GET | List commits for memorial. | Response: `List<MemorialCommitResponseDto>`.
| `/memorials/commit/{commitId}` | GET | Fetch single commit. | Includes content & timestamps.
| `/memorials/pull-request` | POST | Open PR referencing commit. | `{ memorialCommitId }`.
| `/memorials/pull-requests/{memorialId}` | GET | List PRs by memorial. | Response includes `state`, `memorial`, `commit` snapshot.
| `/memorials/pull-request/{requestId}` | GET | Inspect PR details. | Contains latest `state` and metadata.
| `/memorials/pull-request/{requestId}/diff` | GET | Retrieve diff string + conflict indicator. | Used by reviewer UI.
| `/memorials/merge` | PATCH | Merge approved PR. | `{ memorialPullRequestId }`; restricted to chiefs/admin? (needs confirmation).
| `/memorials/reject` | PATCH | Reject PR with message. | `{ memorialPullRequestId }`; user must be authorized.
| `/memorials/resolve` | PATCH | Mark PR as resolved after conflict fix. | `{ memorialPullRequestId, resolved }` message stored.

### 3.4 Flow
1. Contributor submits commit -> receives ID.
2. Contributor opens PR referencing commit.
3. Chief/admin reviews diff via diff endpoint.
4. Reviewer either `merge`, `resolve`, or `reject`.
5. Merge increments memorial version, updates `updatedAt`, triggers downstream events (Kafka) if implemented.

### 3.5 Requirements & Rules
- `content` must be sanitized markdown (max 20k chars); validation TBD.
- Only memorial chiefs or admins can merge/reject/resolve; enforcement occurs in services (extend PRD to specify role checks).
- Each PR transitions states linearly; state machine documented here for QA.
- Diff endpoint must highlight conflicts; `hasConflicts=true` blocks auto-merge until `resolve` called.

### 3.6 Metrics
- Track PR lifecycle durations (`time_to_first_review`, `time_to_merge`).
- Alert when unresolved conflicts older than 48h.

### 3.7 Open Questions
- Should rejection capture textual reason? (Currently only message string from service log.)
- Do we allow multiple open PRs per memorial simultaneously?

## 4. Comments & Social Feedback
### 4.1 Use Cases
- Visitors leave threaded comments (`parentCommentId` optional) on memorial detail pages.
- Users can like/unlike comments; show comment counts and popular threads.

### 4.2 API Surface
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/memorials/comment/{memorial-id}` | POST | Create comment. Body: `{ content, parentCommentId }`.
| `/memorials/comment/{memorial-id}` | GET | Cursor list (default size=10) sorted newest-first.
| `/memorials/comment/{comment-id}` | PATCH | Update comment content.
| `/memorials/comment/{comment-id}` | DELETE | Hard-delete comment.
| `/memorials/comment/{memorial-id}/popular?size=10` | GET | Retrieve top liked comments.
| `/memorials/comment/count?size=10` | GET | Global leaderboard of memorials by comment count.
| `/memorials/comment/likes/{comment-id}` | POST | Like comment.
| `/memorials/comment/likes/{comment-id}` | DELETE | Unlike comment.

### 4.3 Business Rules
- Comments require `user-id`; editing/deleting should validate ownership (service-level requirement).
- Nested replies support single-level recursion via `children` list.
- Response fields: `commentId`, `userId`, `content`, `likes`, `isLiked` (user-specific), `parentId`, `createdAt (yyyy-MM-dd)`, `children`.
- Limit comment length to 1,000 chars (TBD) to prevent abuse.
- Popular endpoint sorts by likes desc, tie-breaker by recent activity.

### 4.4 Metrics & Moderation
- Capture `comment_create_rate`, `comment_flagged_total` (future). Add tooling for spam detection.
- Soft-delete strategy? currently hard delete; evaluate audit needs.

## 5. Memorial Chiefs Management
### 5.1 Goals
- Identify top contributors (“chiefs”) per memorial and expose to clients.
- Provide admin endpoint to recompute chiefs based on bow counts or contributions.

### 5.2 API Surface
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/memorials/chiefs/{memorialId}` | GET | Returns list of chief user IDs / metadata.
| `/memorials/chiefs/update` | GET | Admin-only recalculation job. Requires `role=ADMIN` header.
| `/memorials/chiefs/my` | GET | List memorial IDs for which current user is a chief.

### 5.3 Business Logic
- Update job recalculates chiefs nightly or on-demand; rejects calls without admin role (throws `AuthenticationFailedException`).
- `findMyMemorialIds` returns up to 50 IDs; paginate in future if needed.

### 5.4 Monitoring
- Log recalculation runtime and #memorials affected.
- Alert if update job not run within 24h.

## 6. Anime Catalog & Scheduler
### 6.1 Purpose
- Maintain canonical anime list powering character metadata and filters.
- Provide emergency admin ability to reload anime data via scheduler endpoint (non-public).

### 6.2 API Surface
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/animes` | GET | Cursor-paginated anime list, filterable by `animeName` substring.
| `/animes/{anime-id}` | GET | Fetch single anime.
| `/animes/{anime-id}` | DELETE | Soft-delete anime (used by ops tooling).
| `/animes/schedule` | POST | Trigger `AnimeScheduler.recursiveLoadingAnime()` manually (QA/testing only).

### 6.3 Data Contract
`AnimeResponse` contains `animeId`, `name`, `genres[]`, `imageUrl`.

### 6.4 Requirements
- Cursor pagination default size 20 (client-provided). Validate `size<=50`.
- Delete endpoint should be restricted to admin gateway (doc requirement; implementation TBD).
- Scheduler endpoint must be protected (e.g., internal VPN); though controller lacks auth, gateway policy must enforce.

### 6.5 Metrics
- Track number of anime entries, scheduler duration, failure counts.

## 7. Character Management & Change Requests
### 7.1 Overview
Characters underpin memorial creation; APIs allow CRUD, search, and change requests tied to memorial commits.

### 7.2 Command Endpoints
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/animes/characters` | POST | Create character. Body `CharacterRequest` (animeId, name, age, saying, deathReason, causeOfDeathDetails, deathOfDay, imageUrl).
| `/animes/characters/image` | POST (multipart) | Upload image for authorized user; returns `FileUploadUrlResponse`.
| `/animes/characters/{character-id}` | DELETE | Remove character.
| `/animes/characters/{character-id}` | PATCH | Update existing character with same `CharacterRequest` schema.

Validation: All mandatory `@NotNull` fields enforced; respond 400 on violation.

### 7.3 Query Endpoints
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/animes/characters` | GET | Cursor listing.
| `/animes/characters/{character-id}` | GET | Single character.
| `/animes/characters/search/anime` | GET | Filter by anime IDs list.
| `/animes/characters/search/integrated` | GET | Multi-filter (name, animeId, deathReason, memorialState) using cursor pagination.
| `/animes/characters/search/integrated/offset` | GET | Same filters with offset pagination.
| `/animes/characters/search/death-reason` | GET | Filter by death reason.
| `/animes/characters/search/characterIds` | GET | Batch fetch by IDs.
| `/animes/characters/search/name` | GET | Search by name substring.

### 7.4 Character Change Requests
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/animes/characters/change-requests` | POST | Create change request referencing memorial commit. Body includes `characterId`, `animeId`, `memorialCommitId`, new bio fields.
| `/animes/characters/change-requests/memorial-commit/{memorial-commit-id}` | GET | Fetch change request tied to commit.

Rules:
- Change requests used when commit modifies character biography; ensures reviewer visibility.
- Responses echo fields for QA; stored for audit.

### 7.5 Business Requirements
- Enforce referential integrity: `animeId` & `characterId` must exist; respond 404 otherwise.
- `state` field in `CharacterResponse` indicates memorialization status (`NOT_MEMORIALIZING`, `MEMORIALIZING`). Document semantics for clients.
- Image upload restricts file type to `image/*`, size < 5 MB; returns signed URL for CDN.

### 7.6 Telemetry
- `character_create_total`, `character_update_total`, `character_image_upload_failures`.
- Track search query frequencies to tune indexes.

## 8. Operational & Non-Functional Requirements
- **Rate Limiting**: Apply per-user quotas (e.g., 60 req/min) on write endpoints; heavier on comments & likes to curb spam.
- **Caching**: Use CDN or API gateway caching for GET endpoints with deterministic responses (memorial detail, anime, characters). Invalidate on write events via message bus.
- **Observability**: Standard logging format `traceId userId path status latencyMs`; integrate with Grafana and alert on high error rates.
- **Data Quality**: Nightly job to ensure memorials reference valid characters/anime; log anomalies.
- **Security**: Ensure uploads scanned for malware; restrict scheduler and admin endpoints via ingress policies.

## 9. Risks & Mitigations
| Risk | Impact | Mitigation |
|------|--------|------------|
| High write volume causing contention on memorial tables | Latency spikes | Add DB indexes (`(memorial_id, updated_at)`), consider sharding by memorialId. |
| Unauthorized merge/reject actions | Content integrity compromised | Enforce RBAC in services; log every state transition with actor ID. |
| Comment spam/bot activity | User trust erosion | Introduce CAPTCHA / rate limits; add moderation queue. |
| Scheduler endpoint abuse | Data corruption | Lock behind admin auth and feature flag. |
| Character search latency due to complex filters | Poor UX | Create composite indexes on `(name)`, `(anime_id, memorial_state)`, and use QueryDSL fetch pagination. |

## 10. Open Questions
1. Should memorial list endpoints support flexible page sizes or keep fixed 10?
2. Do chiefs require human-readable metadata (avatars) or only IDs?
3. Who is authorized to trigger merges/rejects? Document roles and propagate from gateway tokens.
4. Do we need to expose comment edit history to admins?
5. Should anime delete be soft (flag) or hard? Need retention policy.
6. How to communicate change-request approval state back to contributors?

