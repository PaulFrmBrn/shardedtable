package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShardedRepository {

    private static final Logger logger = LoggerFactory.getLogger(ShardedRepository.class);

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

    /**
     * Получить TOP-3 по магазинам (максимально много заработавших).
     */
    public List<Summary> getTopStores(int number) {
        Map<Long, BigDecimal> map = shardingService.getAllShards().stream()
                .map(Shard::getMongoTemplate)
                .map(PaymentRepositoryUtils::getStoresTotal)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Summary::getId,
                        Summary::getTotal,
                        BigDecimal::add)
                );

        if (logger.isDebugEnabled()) {
            logger.debug("getTopStore(): all shard summary: {}", map);
        }

        return map.entrySet().stream()
                .sorted((Map.Entry.<Long, BigDecimal>comparingByValue().reversed()))
                .limit(number)
                .map(it -> new Summary(it.getKey(), it.getValue()))
                .collect(Collectors.toList());

    }
}
