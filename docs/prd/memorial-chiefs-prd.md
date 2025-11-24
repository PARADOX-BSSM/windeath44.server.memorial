# Memorial Chiefs PRD

| Version | Date       | Author   | Scope                      |
|---------|------------|----------|----------------------------|
| 0.1     | 2025-11-18 | Codex AI | Chief retrieval + rotation |

## 1. Overview
Memorial chiefs are top contributors recognized per memorial. APIs expose chief lists, allow admins to trigger recalculation, and let users query memorials where they hold chief status.

## 2. Goals
1. Provide up-to-date chief assignments reflecting latest bow counts and contributions.
2. Allow admins to refresh chiefs on demand while preventing unauthorized access.
3. Support UI surfaces for highlighting a user’s memorial stewardship.

## 3. API Surface
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/memorials/chiefs/{memorialId}` | Returns list of chiefs for a memorial. Response structure: `ResponseDto<Object>` (future: define DTO with user IDs + metadata).
| GET | `/memorials/chiefs/update` | Recompute chief assignments. Requires `role=ADMIN` header, otherwise throws `AuthenticationFailedException`.
| GET | `/memorials/chiefs/my` | List memorial IDs where requester is a chief. Requires `user-id` header.

## 4. Business Rules
- Chief calculation criteria (subject to change): combination of bow count, commit contributions, comment impact. Document actual formula once defined.
- `update` endpoint triggers service job `memorialChiefService.updateChiefs()` which may run expensive queries; limit invocation to admin-only and add cooldown (e.g., not more than once per hour).
- `findMyMemorialIds` returns at most 50 IDs; paginate in future release.
- When no chiefs exist for memorial, return empty list (not 404).

## 5. Observability
- Log updates: `chief-update invokedBy=<user> durationMs=<...> updatedMemorials=<count>`.
- Metrics: `chief_update_total`, `chief_update_duration_seconds`, `chief_lookup_total`.
- Alert if update job fails consecutively twice.

## 6. Security
- Validate `role` header equals `ADMIN` before update; consider migrating to token scopes.
- Audit log every admin invocation with IP and timestamp.

## 7. Risks & Open Questions
| Risk | Description | Mitigation |
|------|-------------|------------|
| Expensive recalculation | DB pressure | Run asynchronously and cache results; limit frequency. |
| Unauthorized admin calls | Security breach | Enforce gateway restrictions; add secondary secrets. |
| Stale data | Chiefs outdated | Schedule automatic nightly job; expose `lastUpdatedAt` in response. |

Open Questions:
1. Should we expose chief metadata (avatars, display names) instead of raw IDs?
2. Do chiefs have levels (e.g., head vs co-chief)?
3. Should `update` return job status or run asynchronously with job ID?
