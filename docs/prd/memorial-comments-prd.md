# Memorial Comments & Social Feedback PRD

| Version | Date       | Author   | Scope                              |
|---------|------------|----------|------------------------------------|
| 0.1     | 2025-11-18 | Codex AI | Comments, likes, popularity feeds |

## 1. Overview
The memorial comment system allows threaded discussions beneath each memorial, including reactions (likes), pagination tooling, and popular comment surfacing.

## 2. Goals
1. Enable respectful dialogue with moderation-ready structures.
2. Provide smooth infinite scroll via cursor pagination.
3. Surface popular comments for quick engagement.

## 3. API Surface
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/memorials/comment/{memorial-id}` | Create comment. Body `{ content, parentCommentId }`. Requires `user-id` header.
| GET | `/memorials/comment/{memorial-id}` | Cursor list of comments. Params: `cursorId?`, `size=10`, optional `user-id` to compute `isLiked`.
| PATCH | `/memorials/comment/{comment-id}` | Edit comment. Body `{ content }`. Auth ensures ownership.
| DELETE | `/memorials/comment/{comment-id}` | Delete comment (hard delete).
| GET | `/memorials/comment/{memorial-id}/popular?size=10` | Returns top liked comments.
| GET | `/memorials/comment/count?size=10` | Returns memorials sorted by comment count.
| POST | `/memorials/comment/likes/{comment-id}` | Like comment (requires `user-id`).
| DELETE | `/memorials/comment/likes/{comment-id}` | Unlike comment.

## 4. Business Rules
- Comment length max 1,000 chars; trimmed server-side; empty strings rejected.
- `parentCommentId` optional; must reference same memorial; system supports two-level nesting (parent -> children list in response).
- Editing/deleting allowed only for original author within 24 hours (future requirement; currently unlimited but log changes).
- `MemorialCommentResponse` includes `children[]`; to avoid N+1 queries, service fetches child comments separately and nests them.
- Likes are idempotent: POST toggles to liked state; repeated POST no-op.
- Popular endpoint sorts by `likes desc`, tie by `createdAt desc`.

## 5. Pagination Contracts
- Cursor response: `CursorPage { values[], nextCursorId, hasNext }`.
- `cursorId` parameter refers to last seen comment ID; service fetches comments with ID < cursor for descending order.
- Default `size=10`, max `size=50` to limit payload.

## 6. Observability
- Metrics: `comment_create_total`, `comment_delete_total`, `comment_like_total`, `comment_unlike_total`, `comment_popular_reads_total`.
- Log `userId`, `memorialId`, `commentId`, `action` for moderation.
- Add Prometheus gauge `memorial_comment_backlog` for flagged comments (future).

## 7. Error Handling
| Scenario | Response |
|----------|----------|
| Comment not found | 404.
| Unauthorized edit/delete | 403 (ownership check).
| Invalid parent | 400 (parent belongs to different memorial).
| Duplicate like removal | 204 success (idempotent) or 404 (if no like exists) depending on service design; prefer idempotent success.

## 8. Moderation & Abuse Prevention
- Rate limit comment creation to 30/min per user.
- Add anti-spam heuristics (profanity filter) emitting `comment_flagged_total`.
- Provide admin tooling (future) to soft-delete or hide comments without removing data.

## 9. Risks
| Risk | Impact | Mitigation |
|------|--------|------------|
| Spam wave | UX degradation | Rate limits, CAPTCHA for suspicious accounts.
| Deep nesting causing perf issues | Slow responses | Restrict to two levels and flatten beyond; consider asynchronous loading for replies.
| Like toggling abuse | Inflated counts | Deduplicate via unique constraint `(comment_id, user_id)`.

## 10. Open Questions
1. Should we display edit history to admins?
2. Do we need pinned comments separate from popular ones?
3. Should deleting a parent cascade deleting children or keep them referencing removed parent?
