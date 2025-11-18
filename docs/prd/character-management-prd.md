# Character Management PRD

| Version | Date       | Author   | Scope                                         |
|---------|------------|----------|-----------------------------------------------|
| 0.1     | 2025-11-18 | Codex AI | Character CRUD, search APIs, image uploads |

## 1. Overview
Character records are the backbone of memorials. This PRD covers both command and query endpoints exposed via `/animes/characters` along with file upload support.

## 2. Goals
1. Provide validated creation/update flows ensuring all memorial characters link to existing anime.
2. Offer powerful search capabilities (cursor and offset) for admin & client use cases.
3. Support secure media uploads for character portraits.

## 3. Command Endpoints
| Method | Endpoint | Description | Validation |
|--------|----------|-------------|------------|
| POST | `/animes/characters` | Create character using `CharacterRequest`. | All `@NotNull` fields enforced; `name` unique within anime (future requirement).
| PATCH | `/animes/characters/{character-id}` | Update existing character. | Same schema; partial updates not supported.
| DELETE | `/animes/characters/{character-id}` | Remove character. | Cascade checks ensure no active memorial depends on character.
| POST | `/animes/characters/image` | Upload portrait via multipart form. Requires `user-id`. | Accept `image/*`, size ≤5 MB. Returns `FileUploadUrlResponse` with CDN URL.

`CharacterRequest` fields: `animeId`, `name`, `age`, `saying`, `deathReason`, `causeOfDeathDetails`, `deathOfDay`, `imageUrl`. All but optional details required.

## 4. Query Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/animes/characters` | Cursor list of characters (`cursorId?`, `size`).
| GET | `/animes/characters/{character-id}` | Fetch by ID.
| GET | `/animes/characters/search/anime` | Filter by `animeId` list.
| GET | `/animes/characters/search/integrated` | Cursor-based multi-filter: `name`, `animeId`, `deathReason`, `memorialState`, `size`, `cursorId`.
| GET | `/animes/characters/search/integrated/offset` | Offset variant with `page`, `size`.
| GET | `/animes/characters/search/death-reason` | Filter by `deathReason` string.
| GET | `/animes/characters/search/characterIds` | Batch fetch by IDs.
| GET | `/animes/characters/search/name` | Name search via substring.

## 5. Business Rules
- Cursor pagination sorted by descending `characterId`. `size` default 20, max 50.
- Name search uses case-insensitive `LIKE`; add index on `lower(name)`.
- `memorialState` filter accepts `NOT_MEMORIALIZING` or `MEMORIALIZING`; used for admin dashboards.
- Delete endpoint should soft-delete characters referenced by memorials to avoid orphaning; service currently enforces referential integrity via DB constraints.
- Image upload integrates with object storage; returned URL stored in `imageUrl` field via follow-up PATCH/POST.

## 6. Observability
- Metrics: `character_create_total`, `character_update_total`, `character_delete_total`, `character_image_upload_total`, `character_search_requests_total` (labels per endpoint).
- Log validation failures for debugging.

## 7. Security & Compliance
- Validate image content type by inspecting magic bytes, not only MIME header.
- Rate limit image uploads to 10/min per user.
- Ensure `user-id` header is audited for create/update/delete actions.

## 8. Performance
- Add indexes on `(anime_id)`, `(death_reason)`, `(memorial_state)`, `(name)`.
- For `search/integrated`, use QueryDSL dynamic predicates with limit + fetch join to avoid N+1.

## 9. Risks & Open Questions
| Risk | Impact | Mitigation |
|------|--------|------------|
| Large search result sets | High memory | Force pagination, restrict `size`. |
| Duplicate characters | Confusing UX | Add uniqueness per anime + name, add dedup script. |
| File upload abuse | Storage cost | Enforce quotas, run virus scans. |

Open Questions:
1. Should we support partial updates (PATCH) or keep full replacements?
2. Are there moderation requirements for character bios?
3. Do we need audit logs accessible to compliance teams?
