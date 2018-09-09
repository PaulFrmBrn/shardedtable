package com.paulfrmbrn.sharded.table.sharding.secondary;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = SecondaryRepository.class,
        mongoTemplateRef = "secondaryMongoTemplate")
public class SecondaryMongoConfig {

    @Value("${shard.secondary.host}")
    private String host;

    @Value("${shard.secondary.port}")
    private int port;

    @Value("${shard.secondary.database}")
    private String database;

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        //return new ReactiveMongoTemplate(MongoClients.create("mongodb://" + host + ":" + port), database);
        //return new MongoTemplate(mongoClient(), getDatabaseName());
        return new MongoTemplate(new MongoClient(host, port), database);
    }

}