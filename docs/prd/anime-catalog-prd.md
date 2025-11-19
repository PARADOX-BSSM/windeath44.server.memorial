# Anime Catalog & Scheduler PRD

| Version | Date       | Author   | Scope                                      |
|---------|------------|----------|--------------------------------------------|
| 0.1     | 2025-11-18 | Codex AI | Anime CRUD, search, and manual scheduler |

## 1. Overview
The anime catalog provides canonical metadata powering memorial character creation and filters. This PRD covers REST endpoints under `/animes` plus the scheduler hook for refreshing catalog data.

## 2. Goals
1. Maintain accurate, searchable anime dataset accessible via cursor pagination.
2. Enable admins to remove outdated anime entries.
3. Allow internal teams to trigger the `AnimeScheduler` job during QA or recovery.

## 3. API Inventory
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/animes` | Cursor list. Params: `cursorId?`, `size`, `animeName?` substring filter.
| GET | `/animes/{anime-id}` | Fetch single anime by ID.
| DELETE | `/animes/{anime-id}` | Remove anime (soft delete recommended). Restricted to admins via gateway.
| POST | `/animes/schedule` | Trigger scheduler job `recursiveLoadingAnime()`. Not user-facing; accessible only to ops.

## 4. Business Rules
- Cursor pagination sorted by descending `animeId`. Response: `CursorPage<AnimeResponse>`.
- `size` default 20, max 50; input >50 returns 400.
- `animeName` filter performs case-insensitive `LIKE` query; add index on lower(name).
- Delete action transitions record to `DELETED` state (implementation detail) and prevents further character associations.
- Scheduler endpoint should require admin token even though controller currently lacks enforcement; gateway or filter must block public usage.

## 5. Data Contract
`AnimeResponse { animeId, name, genres[], imageUrl }`.

## 6. Observability
- Metrics: `anime_list_requests_total`, `anime_delete_total`, `anime_scheduler_invocations_total`, `anime_scheduler_duration_seconds`.
- Log scheduler start/end with job outcome.

## 7. Performance
- Ensure `genres` stored separately (normalized) but aggregated via join; caching recommended.
- Scheduler uses external API; enforce timeout/retry logic and run asynchronously to avoid blocking HTTP thread (control via endpoint comment: QA only).

## 8. Risks
| Risk | Impact | Mitigation |
|------|--------|------------|
| Scheduler misuse | Duplicated data | Lock endpoint behind VPN + admin auth; throttle invocations. |
| Delete misuse | Loss of canonical data | Provide confirmation workflow, maintain soft delete, add audit logs. |
| Large dataset | Pagination slow | Use covering indexes and caching layers. |

## 9. Open Questions
1. Should scheduler return job ID/report rather than void response?
2. Do we need POST/PUT endpoints for anime creation/edit, or is scheduler sole writer?
3. Should delete produce tombstone events for other services?
