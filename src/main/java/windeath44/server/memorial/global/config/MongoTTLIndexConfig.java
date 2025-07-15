//package windeath44.server.memorial.global.config;
//
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.index.Index;
//import org.springframework.data.mongodb.core.index.IndexOperations;
//
//@Configuration
//public class MongoTTLIndexConfig {
//
//  @Bean
//  public ApplicationRunner ttlIndexCreator(MongoTemplate mongoTemplate) {
//    return args -> {
//      IndexOperations indexOps = mongoTemplate.indexOps("user_memorial_tracing");
//      indexOps.ensureIndex(new Index()
//              .on("viewed", org.springframework.data.domain.Sort.Direction.ASC)
//              .expire(2592000)); // 30일
//    };
//  }
//}