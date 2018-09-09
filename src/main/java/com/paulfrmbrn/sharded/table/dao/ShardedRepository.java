package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.dao.shrading.ShardingService;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;

public class ShardedRepository {

    private final ShardingService shardingService;

    public ShardedRepository(ShardingService shardingService) {
        this.shardingService = shardingService;
    }

    public void save(Payment payment) {
        shardingService.getShard(payment.getPayerId()).getRepository().save(payment);
    }

    public BigDecimal getPayerTotal(long payerId) {
        MongoTemplate mongoTemplate = shardingService.getShard(payerId).getMongoTemplate();
        return PaymentRepositoryUtils.getPayerTotal(payerId, mongoTemplate);
    }
}
