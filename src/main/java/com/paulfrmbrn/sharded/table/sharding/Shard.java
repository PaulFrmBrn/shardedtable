package com.paulfrmbrn.sharded.table.sharding;

import org.springframework.data.mongodb.core.MongoTemplate;


public class Shard {

    private final int number;

    private final MongoTemplate mongoTemplate;

    public Shard(int number, MongoTemplate mongoTemplate) {
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

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;

    }

}
