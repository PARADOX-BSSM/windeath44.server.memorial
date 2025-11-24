# Character Change Request PRD

| Version | Date       | Author   | Scope                                        |
|---------|------------|----------|----------------------------------------------|
| 0.1     | 2025-11-18 | Codex AI | Character change requests tied to commits |

## 1. Overview
Change requests capture proposed character biography updates originating from memorial commits. They ensure reviewers can trace why a character’s canonical data is changing within the memorial workflow.

## 2. Goals
1. Link every character modification to a memorial commit for auditability.
2. Provide reviewers immediate access to requested changes per commit.
3. Maintain schema parity with `CharacterRequest` while emphasizing provenance fields.

## 3. API Summary
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/animes/characters/change-requests` | Create change request. Body `CharacterChangeRequestDto` with `characterId`, `animeId`, `memorialCommitId`, and new bio fields.
| GET | `/animes/characters/change-requests/memorial-commit/{memorial-commit-id}` | Fetch change request by memorial commit ID.

## 4. Business Rules
- Each memorial commit can have at most one associated change request; duplicate submissions override prior one only if original is unresolved (TBD).
- DTO fields mirror `CharacterRequest`; all annotated `@NotNull` values enforced at validation layer.
- Service verifies that `memorialCommitId` exists and belongs to the same memorial as the target character to prevent cross-link tampering.
- Responses echo stored data for reviewer UI, including `characterApplicationId` primary key.

## 5. Data & Storage
- Persisted entity should include audit fields: `createdAt`, `createdBy`, status (pending/approved/rejected) – currently response lacks state; requirement to extend schema for review workflow.
- Consider linking to PR state machine so acceptance of PR automatically approves change request.

## 6. Observability
- Metrics: `character_change_request_create_total`, `character_change_request_lookup_total`.
- Log creation with `characterId`, `memorialCommitId`, `userId`.

## 7. Risks & Open Questions
| Risk | Impact | Mitigation |
|------|--------|------------|
| Missing status tracking | Hard to know if change applied | Extend DTO with `status` and review endpoints. |
| Incorrect linking | Data corruption | Add DB constraints verifying commit + character relations. |
| Reviewer overload | Longer turnaround | Batch requests and provide filtering tools. |

Open Questions:
1. Should change requests be auto-created when PR touches character content? (Currently manual.)
2. Do we need admin endpoints to approve/reject change requests outside PR merges?
3. How do we notify contributors when their change request is processed?
