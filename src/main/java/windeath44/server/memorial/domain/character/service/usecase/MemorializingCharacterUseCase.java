package windeath44.server.memorial.domain.character.service.usecase;

import windeath44.server.memorial.domain.character.mapper.CharacterMapper;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.service.CharacterService;
import windeath44.server.memorial.global.infrastructure.KafkaProducer;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemorializingCharacterUseCase {
  private final CharacterService characterService;
  private final KafkaProducer kafkaProducer;
  private final CharacterMapper characterMapper;

  @Transactional
  public void memorializing(MemorialAvroSchema memorialAvroSchema) {
    Character character = characterService.findCharacterById(memorialAvroSchema.getCharacterId());
    CharacterAvroSchema characterAvroSchema = characterMapper.toCharacterAvroSchema(character, memorialAvroSchema);

    try {
      Long characterId = characterAvroSchema.getCharacterId();
      characterService.memorializing(characterId);
      kafkaProducer.send("character-memorialized-response", characterAvroSchema);
    } catch (Exception e) {
      log.error("memorializing character fail. characterId: {}", characterAvroSchema.getCharacterId());
      log.error(e.getMessage());
      kafkaProducer.send("character-memorializing-fail-response", characterAvroSchema);
    }

  }
}
