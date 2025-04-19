package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialUpdateHistoryRequestDto(
        String userId,
        Long memorialCommitId
) {
}
