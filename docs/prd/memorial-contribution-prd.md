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

## 6. Business Rules
- Commit content length max 20,000 chars; sanitized markdown (require future validation).
- Each memorial can have multiple open PRs, but merge order is sequential; later merges may conflict.
- Merge action should also create a new memorial commit representing final state for history.
- After merge/reject, notify contributor via event bus (future requirement; currently logging only).

## 7. Observability
- Log every state transition with fields: `prId`, `fromState`, `toState`, `actorUserId`.
- Metrics: `memorial_commit_created_total`, `memorial_pr_open_total`, `memorial_pr_merge_duration_seconds` (time from open to merge), `memorial_pr_reject_total`.
- Alert when `hasConflicts=true` PRs older than 48h.

## 8. Error Handling
| Error | Code | Description |
|-------|------|-------------|
| Memorial not found | 404 | commit creation fails.
| Commit not found | 404 | referencing nonexistent commit/PR.
| Unauthorized action | 403 | user not chief/admin for merge/resolution.
| Invalid state transition | 409 | e.g., merging already merged PR.

## 9. Performance
- Index tables on `(memorial_id, updated_at)` for commit list, `(memorial_id, state)` for PR list.
- Diff generation should run under 200 ms for 20k-char documents; consider server-side caching.

## 10. Risks & Mitigations
| Risk | Impact | Mitigation |
|------|--------|------------|
| Unauthorized merges | Corrupted memorial content | Enforce strict RBAC and audit logs; add approvals from multiple chiefs (future enhancement).
| Content conflicts escalate | Slower throughput | Provide better diff tooling and auto-merge for simple cases.
| Large content slows DB | Query latency | Use text search indexes and store large content in external storage if needed.

## 11. Open Questions
1. Should rejection include reason text visible to contributor?
2. Do we need to limit concurrent PRs per memorial to reduce conflicts?
3. Should merge automatically notify watchers via email/push?
