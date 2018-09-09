package com.paulfrmbrn.sharded.table.sharding;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.Summary;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
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

    public static List<Summary> getTopPayers(int number, MongoTemplate mongoTemplate) {

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

        List<PayerSummary> mappedResults = mongoTemplate.aggregate(
                aggregation,
                Payment.class,
                PayerSummary.class
        ).getMappedResults();

        logger.info("top payers: {}", mappedResults);

        List<Summary> topNPayers = mappedResults.stream()
                .map(payerSummary -> new Summary(payerSummary.payerId, payerSummary.total.bigDecimalValue()))
                .collect(Collectors.toList());

        logger.info("top payers 2: {}", mappedResults);

        return topNPayers;

    }

    public static List<Summary> getStoresTotal(MongoTemplate mongoTemplate) {

        GroupOperation group = group("storeId")
                .last("storeId").as("storeId")
                .sum("sum").as("total");

        ProjectionOperation project = project()
                .andExpression("storeId").as("storeId")
                .andExpression("total").as("total");

        Aggregation aggregation = newAggregation(
                group, project);

        List<StoreSummary> mappedResults = mongoTemplate.aggregate(
                aggregation,
                Payment.class,
                StoreSummary.class
        ).getMappedResults();

        logger.info("all stores: {}", mappedResults);

        List<Summary> topNStores = mappedResults.stream()
                .map(storeSummary -> new Summary(storeSummary.storeId, storeSummary.total.bigDecimalValue()))
                .collect(Collectors.toList());

        logger.info("all stores 2: {}", mappedResults);

        return topNStores;

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
