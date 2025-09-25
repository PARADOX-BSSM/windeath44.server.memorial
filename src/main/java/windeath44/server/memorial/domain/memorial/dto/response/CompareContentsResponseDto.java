package windeath44.server.memorial.domain.memorial.dto.response;

public record CompareContentsResponseDto(
        Boolean mergeable,
        String conflict
) {
  @Override
  public String toString() {
    return "CompareContentsResponseDto{" +
            "mergeable=" + mergeable +
            ", conflict='" + conflict + '\'' +
            '}';
  }
}
