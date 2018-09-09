package com.paulfrmbrn.sharded.table.sharding.primary;

import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class PrimaryMongoConfig {

    @Value("${shard.primary.host}")
    private String host;

    @Value("${shard.primary.port}")
    private int port;

    @Value("${shard.primary.database}")
    private String database;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public ReactiveMongoTemplate primaryMongoTemplate() throws Exception {
        return new ReactiveMongoTemplate(MongoClients.create("mongodb://" + host + ":" + port), database);
    }
}
