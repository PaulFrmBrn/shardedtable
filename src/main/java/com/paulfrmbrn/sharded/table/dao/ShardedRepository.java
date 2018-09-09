package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.Summary;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShardedRepository {

    private final ShardingService shardingService;

    public ShardedRepository(ShardingService shardingService) {
        this.shardingService = shardingService;
    }

    public void save(Payment payment) {
        shardingService.getShard(payment.getPayerId()).getRepository().save(payment);
    }

    /**
     * Выдать общую сумму потраченных средств по отправителю;
     */
    public BigDecimal getPayerTotal(long payerId) {
        MongoTemplate mongoTemplate = shardingService.getShard(payerId).getMongoTemplate();
        return PaymentRepositoryUtils.getPayerTotal(payerId, mongoTemplate);
    }

    /**
     * Получить TOP-3 отправителей (максимально много заплативших);
     */
    public List<Summary> getTopPayers(int number) {
        return shardingService.getAllShards().stream()
                .map(Shard::getMongoTemplate)
                .map(template -> PaymentRepositoryUtils.getTopPayers(number, template))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Summary::getTotal).reversed())
                .limit(number)
                .collect(Collectors.toList());

    }
}
