package com.paulfrmbrn.sharded.table.sharding.primary;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = PrimaryRepository.class,
        mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig {

    @Value("${shard.primary.host}")
    private String host;

    @Value("${shard.primary.port}")
    private int port;

    @Value("${shard.primary.database}")
    private String database;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(new MongoClient(host, port), database);
    }
}
