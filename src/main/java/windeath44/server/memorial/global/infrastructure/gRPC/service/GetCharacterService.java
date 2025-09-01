package windeath44.server.memorial.global.infrastructure.gRPC.service;

import com.example.grpc.GetCharacterRequest;
import com.example.grpc.GetCharacterResponse;
import com.example.grpc.GetCharacterServiceGrpc;
import windeath44.server.memorial.domain.character.exception.NotFoundCharacterException;
import windeath44.server.memorial.domain.character.mapper.CharacterMapper;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.service.CharacterService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GetCharacterService extends GetCharacterServiceGrpc.GetCharacterServiceImplBase {
  private final CharacterService characterService;
  private final CharacterMapper characterMapper;

  @Override
  public void getCharacter(GetCharacterRequest request, StreamObserver<GetCharacterResponse> responseObserver) {
    Long characterId = request.getCharacterId();
    try {
      Character character = characterService.findById(characterId);
      GetCharacterResponse getCharacterResponse = characterMapper.toGetCharacterResponse(character);

      responseObserver.onNext(getCharacterResponse);
      responseObserver.onCompleted();

    } catch (NotFoundCharacterException e) {
      responseObserver.onError(Status.NOT_FOUND
              .withDescription(e.getMessage())
              .asRuntimeException());
    }

  }
}
