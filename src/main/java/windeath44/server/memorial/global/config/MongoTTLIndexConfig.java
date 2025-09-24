package windeath44.server.memorial.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Slf4j
@Configuration
public class MongoTTLIndexConfig {

  @Bean
  public ApplicationRunner ttlIndexCreator(MongoTemplate mongoTemplate) {
    return args -> {
      try {
        IndexOperations indexOps = mongoTemplate.indexOps("user_memorial_tracing");
        indexOps.ensureIndex(new Index()
                .on("viewed", org.springframework.data.domain.Sort.Direction.ASC)
                .expire(2592000)); // 30일
        log.info("TTL index created successfully for user_memorial_tracing collection");
      } catch (Exception e) {
        log.warn("Failed to create TTL index for user_memorial_tracing collection: {}", e.getMessage());
        // 인덱스 생성 실패시에도 애플리케이션 시작을 중단하지 않음
      }
    };
  }
}