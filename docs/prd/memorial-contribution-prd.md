# Memorial Contribution Workflow PRD

| Version | Date       | Author   | Scope                            |
|---------|------------|----------|----------------------------------|
| 0.1     | 2025-11-18 | Codex AI | Commits, pull requests, reviews |

## 1. Overview
Contributors collaboratively edit memorial content through a git-inspired workflow: users create commits, open pull requests (PRs), and chiefs/admins review to merge, resolve conflicts, or reject submissions. This document specifies business behavior for all endpoints under `/memorials/*` related to content contributions.

## 2. Goals
1. Ensure every memorial edit is auditable with immutable commit history.
2. Provide reviewers with enough information (diffs, state machine transitions) to make decisions quickly.
3. Maintain integrity by allowing only authorized users to merge/reject/resolve PRs.

## 3. Entities
- **MemorialCommit**: Draft text tied to `memorialId`, contains `content`, `userId`, `createdAt`.
- **MemorialPullRequest**: Wraps commit and memorial, tracks `state` (enum: `PENDING`, `RESOLVED`, `APPROVED`, `REJECTED`, `STORED`).
- **Diff**: Snapshot difference between memorial current content and commit content; indicates `hasConflicts`.

## 4. API Inventory
| Method | Endpoint | Actor | Description |
|--------|---------|-------|-------------|
| POST | `/memorials/commit` | Contributor | Create commit from user input. Body: `{ memorialId, content }`. Returns commit ID.
| GET | `/memorials/commits/{memorialId}` | Any | List commits for memorial.
| GET | `/memorials/commit/{commitId}` | Any | Fetch single commit metadata/content.
| POST | `/memorials/pull-request` | Contributor | Open PR referencing commit. Body: `{ memorialCommitId }`.
| GET | `/memorials/pull-requests/{memorialId}` | Any | List PRs with states + metadata.
| GET | `/memorials/pull-request/{requestId}` | Any | Retrieve PR details.
| GET | `/memorials/pull-request/{requestId}/diff` | Reviewer | Provide diff and conflict flag.
| PATCH | `/memorials/merge` | Chief/Admin | Merge approved PR. Body: `{ memorialPullRequestId }`.
| PATCH | `/memorials/resolve` | Chief/Admin | Mark conflicts resolved with note. Body: `{ memorialPullRequestId, resolved }`.
| PATCH | `/memorials/reject` | Chief/Admin | Reject PR. Body: `{ memorialPullRequestId }` (future: reason field).

## 5. Workflow States
1. **Draft Commit** created (not live).
2. **Pull Request PENDING** when opened.
3. If conflicts detected, `hasConflicts=true`. Reviewer can request updates; once contributor fixes, call `/resolve` with note -> `RESOLVED`.
4. Approved PR (state `APPROVED`) then merged via `/merge`; memorial content updates and `updatedAt` refreshes.
5. Rejected PR -> `REJECTED`, no content change.
6. `STORED` reserved for archived PRs.

State transition rules:
- Only chiefs/admins can call merge/reject/resolve. Enforce via service-level RBAC (gateway ensures `role` header or membership; doc requirement).
- `merge` allowed only when PR `APPROVED` or `RESOLVED`.
- `resolve` requires textual `resolved` summary for audit logs.

## 6. Detailed Entity-Level Logic
### 6.1 MemorialCommit Creation
1. **Request validation**: ensure `memorialId` exists via `MemorialRepository`. Reject with 404 if missing.
2. **Entity construction**: instantiate `MemorialCommit` with fields:
   - `memorialCommitId` (auto-generated)
   - `memorialId`
   - `userId` from `user-id` header
   - `content`
   - `createdAt` (server timestamp)
3. **Persistence**: save via `MemorialCommitRepository`. No mutation to `Memorial` occurs yet.
4. **Response**: return commit ID for later reference.

### 6.2 Pull Request Lifecycle (Create & Fetch)
1. **Validation**: confirm referenced `MemorialCommit` exists and is not already bound to an open PR.
2. **Entity fields**: `MemorialPullRequest` stores
   - `memorialPullRequestId`
   - `memorialId` (inferred from commit)
   - `memorialCommitId`
   - `userId` (author)
   - `state = PENDING`
   - `createdAt`, `updatedAt`
3. **Diff snapshot**: on creation, compute diff vs. current `Memorial` content and persist `MemorialPullRequestDiff` entity (`diffContent`, `hasConflicts`, `createdAt`).
4. **Exposure**: GET endpoints join `Memorial`, `MemorialCommit`, and diff info to produce DTOs for reviewer dashboards.

### 6.3 Merge Execution
1. **Preconditions**: `state` must be `APPROVED` or `RESOLVED`; caller must be memorial chief or admin (validated via `MemorialChiefService` or role header).
2. **Transactional steps**:
   - Lock target `Memorial` row.
   - Apply commit `content` to memorial, updating `memorial.content`, `memorial.memorialCommitId`, `memorial.updatedAt`, optionally `mergerId` with acting user.
   - Increment memorial version/bow counts unaffected.
   - Transition PR `state -> APPROVED` (if auto) -> `STORED` or `MERGED` (implementation detail) and stamp `updatedAt`.
   - Persist audit record summarizing merge (e.g., `MemorialMergeHistory`).
3. **Post-actions**: emit domain event (`MemorialMergedEvent`) for downstream caches; delete or archive diff entry.

### 6.4 Conflict Resolution Flow
1. When `hasConflicts=true`, reviewer instructs contributor to resolve externally.
2. Contributor updates commit (new commit) and reopens PR or updates existing commit (TBD). Once ready, reviewer calls `/memorials/resolve` with payload `{ memorialPullRequestId, resolved }`.
3. Service ensures PR currently `PENDING` with conflicts. It updates state to `RESOLVED`, stores `resolved` note on `MemorialPullRequest` entity (new field) and timestamps action. No memorial content change yet.
4. After resolution, merge proceeds via `/merge` using same logic as above.

### 6.5 Reject Flow
1. Authorized reviewer hits `/memorials/reject` with `{ memorialPullRequestId }` (future: include reason text).
2. Service verifies PR `state` in {`PENDING`, `RESOLVED`} to prevent double rejection.
3. Updates entity `state=REJECTED`, `updatedAt=now`, `rejectedBy=userId`. Persist optional rejection note column.
4. PR remains in history; memorial entity unchanged. Notify contributor asynchronously.

## 7. Business Rules
- Commit content length max 20,000 chars; sanitized markdown (require future validation).
- Each memorial can have multiple open PRs, but merge order is sequential; later merges may conflict.
- Merge action should also create a new memorial commit representing final state for history.
- After merge/reject, notify contributor via event bus (future requirement; currently logging only).

## 8. Observability
- Log every state transition with fields: `prId`, `fromState`, `toState`, `actorUserId`.
- Metrics: `memorial_commit_created_total`, `memorial_pr_open_total`, `memorial_pr_merge_duration_seconds` (time from open to merge), `memorial_pr_reject_total`.
- Alert when `hasConflicts=true` PRs older than 48h.

## 9. Error Handling
| Error | Code | Description |
|-------|------|-------------|
| Memorial not found | 404 | commit creation fails.
| Commit not found | 404 | referencing nonexistent commit/PR.
| Unauthorized action | 403 | user not chief/admin for merge/resolution.
| Invalid state transition | 409 | e.g., merging already merged PR.

## 10. Performance
- Index tables on `(memorial_id, updated_at)` for commit list, `(memorial_id, state)` for PR list.
- Diff generation should run under 200 ms for 20k-char documents; consider server-side caching.

## 11. Risks & Mitigations
| Risk | Impact | Mitigation |
|------|--------|------------|
| Unauthorized merges | Corrupted memorial content | Enforce strict RBAC and audit logs; add approvals from multiple chiefs (future enhancement).
| Content conflicts escalate | Slower throughput | Provide better diff tooling and auto-merge for simple cases.
| Large content slows DB | Query latency | Use text search indexes and store large content in external storage if needed.

## 12. Open Questions
1. Should rejection include reason text visible to contributor?
2. Do we need to limit concurrent PRs per memorial to reduce conflicts?
3. Should merge automatically notify watchers via email/push?
