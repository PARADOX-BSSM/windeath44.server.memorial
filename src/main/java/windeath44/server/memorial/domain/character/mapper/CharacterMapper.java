package windeath44.server.memorial.domain.character.mapper;

import windeath44.server.memorial.grpc.GetCharacterResponse;
import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.dto.response.CharacterIdResponse;
import windeath44.server.memorial.domain.character.dto.response.CharacterResponse;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.model.type.CauseOfDeath;
import windeath44.server.memorial.domain.character.model.type.CharacterState;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterMapper {

  public Character toCharacter(CharacterRequest characterRequest, Anime anime) {
    String name = characterRequest.name();
    CauseOfDeath deathReason = CauseOfDeath.valueOfDeathReason(characterRequest.deathReason());
    Integer age = characterRequest.age();
    String deathOfDay = characterRequest.deathOfDay();
    String saying = characterRequest.saying();
    String causeOfDeathDetails = characterRequest.causeOfDeathDetails();

    return Character.builder()
            .anime(anime)
            .name(name)
            .age(age)
            .saying(saying)
            .deathReason(deathReason)
            .causeOfDeathDetails(causeOfDeathDetails)
            .deathOfDay(deathOfDay)
            .build();
  }

  public CharacterResponse toCharacterResponse(Character character) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    String deathReason = character.getDeathReason();
    String causeOfDeathDetails = character.getCauseOfDeathDetails();
    String imageUrl = character.getImageUrl();
    Long bow_count = character.getBowCount();
    String deathOfDay = character.getDeathOfDay();
    CharacterState state = character.getState();
    Long animeId = character.getAnimeId();
    Integer age = character.getAge();
    String saying = character.getSaying();

    return CharacterResponse.builder()
            .characterId(characterId)
            .animeId(animeId)
            .name(name)
            .deathReason(deathReason)
            .causeOfDeathDetails(causeOfDeathDetails)
            .imageUrl(imageUrl)
            .bowCount(bow_count)
            .deathOfDay(deathOfDay)
            .state(state.toString())
            .age(age)
            .saying(saying)
            .build();
  }

  public GetCharacterResponse toGetCharacterResponse(Character character) {
    Anime anime = character.getAnime();
    Long animeId = anime.getAnimeId();
    String animeName = anime.getName();

    String name = character.getName();
    String state = character.getState().toString();
    String characterSaying = character.getSaying();

    GetCharacterResponse response = GetCharacterResponse.newBuilder()
            .setAnimeId(animeId)
            .setAnimeName(animeName)
            .setName(name)
            .setContent(characterSaying)
            .setState(state)
            .build();
    return response;

  }

  public CharacterAvroSchema toCharacterAvroSchema(Character character, MemorialAvroSchema memorialAvroSchema) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    String content = "";
    String deathReason = character.getDeathReason();
    String state = character.getState().toString();
    String applicantId = memorialAvroSchema.getWriterId();

    CharacterAvroSchema characterAvroSchema = CharacterAvroSchema.newBuilder()
            .setCharacterId(characterId)
            .setName(name)
            .setContent(content)
            .setDeathReason(deathReason)
            .setState(state)
            .setApplicantId(applicantId)
            .build();
    return characterAvroSchema;
  }

  public List<CharacterResponse> toCharacterListResponse(Slice<Character> characterSlice) {
    return characterSlice.getContent()
            .stream()
            .map(this::toCharacterResponse)
            .toList();
  }

  public CharacterIdResponse toCharacterIdResponse(Character savedCharacter) {
    return new CharacterIdResponse(savedCharacter.getCharacterId());
  }
}
