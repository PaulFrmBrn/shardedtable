package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.Payment;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

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

        Criteria criteria = where("payerId").is(payerId);
        GroupOperation group = group("payerId")
                .last("payerId").as("payerId")
                .addToSet("storeId").as("storeIds")
                .sum("sum").as("total")
                .last("sum").as("lastSum");
        ProjectionOperation projection = project("total", "lastSum", "storeIds").and("payerId").previousOperation();

        Aggregation aggregation = newAggregation(
                match(criteria),
                group,
                projection);

        AggregationResults<PaymentSummary> aggregate = mongoTemplate.aggregate(aggregation, Payment.class, PaymentSummary.class);

        List<PaymentSummary> mappedResults = aggregate.getMappedResults();

        logger.debug("mappedResults = {}", mappedResults);

        if (mappedResults.size() == 0) {
            return BigDecimal.ZERO;
        } else {
            PaymentSummary summary = mappedResults.get(0);
            logger.debug("summary = {}", summary);
            Decimal128 total = summary.getTotal();
            logger.debug("total = {}", total);
            BigDecimal totalBD = total.bigDecimalValue();
            logger.debug("total (BigDecial) = {}", totalBD);
            return totalBD;
        }

    }

    // todo rename
    // try all private
    class PaymentSummary {


        private Decimal128 total;

        private Long payerId;

        private List<Long> storeIds;

        private Decimal128 lastSum;

        public PaymentSummary() {
        }

        public Decimal128 getTotal() {
            return total;
        }

        public void setTotal(Decimal128 total) {
            logger.debug("set total = {}", total);
            this.total = total;
        }

        public Long getPayerId() {
            return payerId;
        }

        public void setPayerId(Long payerId) {
            this.payerId = payerId;
        }

        public List<Long> getStoreIds() {
            return storeIds;
        }

        public void setStoreIds(List<Long> storeIds) {
            logger.debug("set storeIds = {}", storeIds);
            this.storeIds = storeIds;
        }

        public Decimal128 getLastSum() {
            return lastSum;
        }

        public void setLastSum(Decimal128 lastSum) {
            this.lastSum = lastSum;
        }

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
