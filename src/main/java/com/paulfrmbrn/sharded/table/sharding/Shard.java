package com.paulfrmbrn.sharded.table.sharding;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;


public class Shard {

    private final int number;

    private final ReactiveMongoTemplate mongoTemplate;

    public Shard(int number, ReactiveMongoTemplate mongoTemplate) {
        this.number = number;
        this.mongoTemplate = mongoTemplate;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Shard{" +
                "number=" + number +
                '}';
    }

    public ReactiveMongoTemplate getMongoTemplate() {
        return mongoTemplate;

    }

}
