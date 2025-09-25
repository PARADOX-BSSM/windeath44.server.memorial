package windeath44.server.memorial.domain.memorial.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.model.MemorialTracing;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class MemorialTracingCustomRepositoryImpl implements MemorialTracingCustomRepository {

  private final MongoTemplate mongoTemplate;

  @Override
  public List<MemorialTracing> findRecentByUserId(String userId, int size) {
    Aggregation aggregation = newAggregation(
        match(Criteria.where("userId").is(userId)),
        sort(org.springframework.data.domain.Sort.Direction.DESC, "viewed"),
        group("memorialId").first("$$ROOT").as("latestDoc"),
        replaceRoot("latestDoc"),
        sort(org.springframework.data.domain.Sort.Direction.DESC, "viewed"),
        limit(size)
    );

    AggregationResults<MemorialTracing> results = mongoTemplate.aggregate(
        aggregation, "user_memorial_tracing", MemorialTracing.class);

    return results.getMappedResults();
  }

  @Override
  public List<MemorialTracing> findRecentByUserIdWithCursor(String userId, Date cursor, int size) {
    Aggregation aggregation = newAggregation(
        match(Criteria.where("userId").is(userId).and("viewed").lt(cursor)),
        sort(org.springframework.data.domain.Sort.Direction.DESC, "viewed"),
        group("memorialId").first("$$ROOT").as("latestDoc"),
        replaceRoot("latestDoc"),
        sort(org.springframework.data.domain.Sort.Direction.DESC, "viewed"),
        limit(size)
    );

    AggregationResults<MemorialTracing> results = mongoTemplate.aggregate(
        aggregation, "user_memorial_tracing", MemorialTracing.class);

    return results.getMappedResults();
  }
}