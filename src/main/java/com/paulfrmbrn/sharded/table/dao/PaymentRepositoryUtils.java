package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.Payment;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

// todo rename
// todo utils class
public class PaymentRepositoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepositoryUtils.class);

    public static BigDecimal getPayerTotal(long payerId, MongoTemplate mongoTemplate) {

        logger.info("getPayerTotal(): {}", payerId);

        Aggregation aggregation = newAggregation(
                match(where("payerId").is(payerId)),
                group("payerId")
                        .last("payerId").as("payerId")
                        .addToSet("storeId").as("storeIds")
                        .sum("sum").as("total")
                        .last("sum").as("lastSum"),
                project("total", "lastSum", "storeIds").and("payerId").previousOperation());

        List<PaymentSummary> mappedResults = mongoTemplate.aggregate(
                aggregation,
                Payment.class,
                PaymentSummary.class
        ).getMappedResults();

        logger.debug("mappedResults = {}", mappedResults);

        if (mappedResults.size() == 0) {
            return BigDecimal.ZERO;
        } else {
            PaymentSummary summary = mappedResults.get(0);
            BigDecimal total = summary.total.bigDecimalValue();
            logger.info("payer total: payerId={}, total={}", payerId, total);
            return total;
        }

    }

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
