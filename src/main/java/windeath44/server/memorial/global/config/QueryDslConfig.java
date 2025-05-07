package windeath44.server.memorial.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
  @Bean
  public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
    JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
    return jpaQueryFactory;
  }
}
