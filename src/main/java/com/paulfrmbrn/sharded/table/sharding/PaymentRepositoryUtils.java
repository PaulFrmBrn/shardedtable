package com.paulfrmbrn.sharded.table.sharding;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.Summary;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

// todo rename
// todo utils class
public final class PaymentRepositoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepositoryUtils.class);

    private PaymentRepositoryUtils() {
    }

    public static Flux<BigDecimal> getPayerTotal(long payerId, ReactiveMongoTemplate mongoTemplate) {

        logger.info("getPayerTotal(): {}", payerId);

        Aggregation aggregation = newAggregation(
                match(where("payerId").is(payerId)),
                group("payerId")
                        .last("payerId").as("payerId")
                        .addToSet("storeId").as("storeIds")
                        .sum("sum").as("total")
                        .last("sum").as("lastSum"),
                project("total", "lastSum", "storeIds").and("payerId").previousOperation());

        return mongoTemplate.aggregate(aggregation, Payment.class, PaymentSummary.class)
                .map(summary -> {
                    BigDecimal total = summary.total.bigDecimalValue();
                    logger.info("payer total: payerId={}, total={}", payerId, total);
                    return total;
                });
    }

    public static Flux<Summary> getTopPayers(int number, ReactiveMongoTemplate mongoTemplate) {

        logger.info("getTopPayers(): {}", number);

        GroupOperation group = group("payerId")
                .last("payerId").as("payerId")
                .sum("sum").as("total");
        SortOperation sort = sort(new Sort(Sort.Direction.DESC, "total"));
        LimitOperation limit = limit(number);

        ProjectionOperation project = project()
                .andExpression("payerId").as("payerId")
                .andExpression("total").as("total");

        Aggregation aggregation = newAggregation(
                group, sort, limit, project);

        return mongoTemplate.aggregate(aggregation, Payment.class, PayerSummary.class)
                .map(summary -> new Summary(summary.payerId, summary.total.bigDecimalValue()));

    }

    public static Flux<Summary> getStoresTotal(ReactiveMongoTemplate mongoTemplate) {

        logger.info("getStoresTotal()");

        GroupOperation group = group("storeId")
                .last("storeId").as("storeId")
                .sum("sum").as("total");

        ProjectionOperation project = project()
                .andExpression("storeId").as("storeId")
                .andExpression("total").as("total");

        Aggregation aggregation = newAggregation(
                group, project);

        return mongoTemplate.aggregate(aggregation, Payment.class, StoreSummary.class)
                .map(summary -> new Summary(summary.storeId, summary.total.bigDecimalValue()));

    }

    // todo rename MongoSummary
    private class StoreSummary {
        private Long storeId;
        private Decimal128 total;

        @Override
        public String toString() {
            return "StoreSummary{" +
                    "storeId=" + storeId +
                    ", total=" + total +
                    '}';
        }
    }

    // todo rename MongoSummary
    private class PayerSummary {
        private Long payerId;
        private Decimal128 total;

        @Override
        public String toString() {
            return "PayerSummary{" +
                    "payerId=" + payerId +
                    ", total=" + total +
                    '}';
        }
    }

    // todo comabine with MongoSummary
    // todo rename
    private class PaymentSummary {

        private Decimal128 total;
        private Long payerId;
        private List<Long> storeIds;
        private Decimal128 lastSum;

        @Override
        public String toString() {
            return "PaymentSummary{" +
                    "total=" + total +
                    ", payerId=" + payerId +
                    ", storeIds=" + storeIds +
                    ", lastSum=" + lastSum +
                    '}';
        }
    }
}
