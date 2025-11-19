# Memorial Discovery & Filtering PRD

| Version | Date       | Author   | Scope                           |
|---------|------------|----------|---------------------------------|
| 0.1     | 2025-11-18 | Codex AI | All read-only memorial listings |

## 1. Overview
The memorial discovery experience enables clients to fetch memorial metadata in multiple ways: direct ID lookup, bulk retrieval, ordered pagination, and character-based filtering. Consistency and predictable ordering are crucial for caching and for bridging mobile and web clients.

## 2. Goals
1. Deliver sub-200 ms responses for all listing endpoints under 500 rps sustained load.
2. Ensure deterministic ordering with validated `orderBy` options.
3. Support analytics via tracing whenever a known user views a memorial.

## 3. API Summary
| Method | Endpoint | Description | Notes |
|--------|----------|-------------|-------|
| GET | `/memorials/{memorialId}` | Fetch a single memorial enriched with chiefs, bow count, and commit metadata. | Optional `user-id` header triggers `MemorialTracingEvent`.
| GET | `/memorials/memorialIds?memorialIds=1,2` | Retrieve multiple memorials by ID list (max 50). | Returns `List<MemorialResponseDto>` preserving request order.
| GET | `/memorials?orderBy={strategy}&page={n}` | Offset-pagination listing sorted by strategy. | Page size fixed at 10; `page` is zero-based `Long`.
| POST | `/memorials/character-filtered` | Listing filtered by characters. | Body: `{ "characters": [ids], "orderBy": "...", "page": 0 }`.

## 4. Business Logic
- Order strategies allowed: `recently-updated`, `lately-updated`, `ascending-bow-count`, `descending-bow-count`. Unknown values throw `UndefinedOrderByException` (400).
- Pagination uses repository-level `OffsetPage` with `limit=10`. When no results are found for page > max, service throws `MemorialNotFoundException` to signal empty dataset (clients treat as end-of-feed).
- Character filter accepts up to 20 IDs; duplicates are ignored server-side.
- `MemorialResponseDto` exposes: `memorialId`, `characterId`, `chiefs[]`, `bowCount`, `memorialCommitId`, `content`, `userId`, `createdAt`, `mergerId`, `updatedAt`.

## 5. Data Flow
1. Controller delegates to `MemorialGetService`.
2. Service validates `orderBy` and optionally publishes `MemorialTracingEvent` if user header present.
3. Repository queries (QueryDSL) join memorial data, characters, commit info.
4. Response serialized in `ResponseDto` envelope.

## 6. Observability & Metrics
- Emit `memorial_lookup_total` w/ labels `type=single|bulk|list|filtered` and `result=hit|miss`.
- Publish tracing event only when `user-id` header exists to avoid noise.
- Log `orderBy`, `page`, and `size` to monitor usage patterns.

## 7. Performance Requirements
- Add DB indexes: `(updated_at DESC)`, `(bow_count)` to sustain ordering queries.
- Bulk lookup uses `WHERE id IN (...)` with limit 50; consider caching results in Redis keyed by memorialId.
- For filtered endpoints, add join table index `(character_id, memorial_id)`.

## 8. Error Handling
| Scenario | Code | Behavior |
|----------|------|----------|
| Unknown memorial | 404 | `MemorialNotFoundException`.
| Empty list for page beyond range | 404 | Treated as not found to signal UI to stop.
| Missing/invalid `orderBy` | 400 | `UndefinedOrderByException`.
| Invalid `memorialIds` input | 400 | Spring binding error; respond with message.

## 9. Risks
| Risk | Impact | Mitigation |
|------|--------|------------|
| Heavy pagination load | DB CPU spikes | Introduce caching and asynchronous prefetching; consider search index (Elasticsearch) long term. |
| Character filter query complexity | Slow responses | Limit filter size, add indexes, consider denormalized table. |
| Lack of user header analytics | Reduced personalization | Encourage clients to send `user-id` when available; fallback is anonymous view. |

## 10. Open Questions
1. Do we need flexible page sizes? (Currently locked to 10.)
2. Should we expose `totalCount` for pagination or keep clients infinite-scroll only?
3. Are there SEO requirements for certain memorial detail pages requiring caching hints?
