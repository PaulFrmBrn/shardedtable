package com.paulfrmbrn.sharded.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepositoryCustomImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;


    // todo refactor
    @Override
    public Mono<BigDecimal> getTotalForPayer(long payerId) {

//        Query query = new Query();
//        query.addCriteria(Criteria.where("payerId").is(payerId));
//        List<Payment> payments = mongoTemplate.find(query, Payment.class);
//        logger.info("payments = {}", payments);

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
            return Mono.empty();
        } else {
            PaymentSummary summary = mappedResults.get(0);
            logger.debug("summary = {}", summary);
            BigDecimal total = summary.getTotal();
            logger.debug("total = {}", total);
            return Mono.just(total);
        }

    }

    // todo rename
    // try all private
    class PaymentSummary {


        private BigDecimal total;

        private Long payerId;

        private List<Long> storeIds;

        private BigDecimal lastSum;

        public PaymentSummary() {
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
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

        public BigDecimal getLastSum() {
            return lastSum;
        }

        public void setLastSum(BigDecimal lastSum) {
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
