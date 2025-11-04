package windeath44.server.memorial.domain.memorial.dto.response;

public record MemorialPullRequestDiffResponseDto(
        Long memorialPullRequestId,
        String diffContent,
        Boolean hasConflicts,
        String userId,
        java.time.LocalDateTime createdAt
) {
}