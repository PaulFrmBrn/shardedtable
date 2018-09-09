package com.paulfrmbrn.sharded.table.sharding;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.Summary;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public final class PaymentRepositoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepositoryUtils.class);

    private PaymentRepositoryUtils() {
    }

    public static Flux<BigDecimal> getPayerTotal(long payerId, ReactiveMongoTemplate mongoTemplate) {

        logger.info("getPayerTotal(): {}", payerId);

        Aggregation aggregation = newAggregation(
                match(where("payerId").is(payerId)),
                group("payerId")
                        .last("payerId").as("id")
                        .sum("sum").as("total"),
                project("total").and("id").previousOperation());

        return mongoTemplate.aggregate(aggregation, Payment.class, MongoSummary.class)
                .map(summary -> {
                    BigDecimal total = summary.total.bigDecimalValue();
                    logger.info("payer total: payerId={}, total={}", payerId, total);
                    return total;
                });
    }

    public static Flux<Summary> getTopPayers(int number, ReactiveMongoTemplate mongoTemplate) {

        logger.info("getTopPayers(): {}", number);

        Aggregation aggregation = newAggregation(
                group("payerId")
                        .last("payerId").as("id")
                        .sum("sum").as("total"),
                sort(new Sort(Sort.Direction.DESC, "total")),
                limit(number),
                project()
                        .andExpression("id").as("id")
                        .andExpression("total").as("total"));

        return mongoTemplate.aggregate(aggregation, Payment.class, MongoSummary.class)
                .map(summary -> new Summary(summary.id, summary.total.bigDecimalValue()));

    }

    public static Flux<Summary> getStoresTotal(ReactiveMongoTemplate mongoTemplate) {

        logger.info("getStoresTotal()");

        Aggregation aggregation = newAggregation(
                group("storeId")
                        .last("storeId").as("id")
                        .sum("sum").as("total"), project()
                        .andExpression("id").as("id")
                        .andExpression("total").as("total"));

        return mongoTemplate.aggregate(aggregation, Payment.class, MongoSummary.class)
                .map(summary -> new Summary(summary.id, summary.total.bigDecimalValue()));

    }

    private class MongoSummary {
        private Long id;
        private Decimal128 total;

        @Override
        public String toString() {
            return "StoreSummary{" +
                    "id=" + id +
                    ", total=" + total +
                    '}';
        }
    }

}
