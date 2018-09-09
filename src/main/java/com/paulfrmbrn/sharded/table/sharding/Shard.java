package com.paulfrmbrn.sharded.table.sharding;

import com.paulfrmbrn.sharded.table.Payment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public class Shard {

    private final int number;

    private final MongoRepository<Payment, String> repository;

    private final MongoTemplate mongoTemplate;

    public Shard(int number, MongoRepository<Payment, String> repository, MongoTemplate mongoTemplate) {
        this.number = number;
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public int getNumber() {
        return number;
    }

    public MongoRepository<Payment, String> getRepository() {
        return repository;
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
