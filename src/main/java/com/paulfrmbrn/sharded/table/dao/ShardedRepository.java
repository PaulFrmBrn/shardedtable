package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.dao.shrading.ShardingService;

public class ShardedRepository {

    private final ShardingService shardingService;

    public ShardedRepository(ShardingService shardingService) {
        this.shardingService = shardingService;
    }

    public void save(Payment payment) {
        shardingService.getShard(payment.getStoreId()).getRepository().save(payment);
    }
}
