package windeath44.server.memorial.domain.memorial.dto.response;

public record MemorialBowStatusResponseDto(
        boolean canBow,
        String availableAt
) {
}
