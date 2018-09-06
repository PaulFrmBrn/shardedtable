package com.paulfrmbrn.sharded.table.primary;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.paulfrmbrn.sharded.table.primary", mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig {

    @Value("${mongodb.primary.host}")
    private String host;

    @Value("${mongodb.primary.port}")
    private int port;

    @Value("${mongodb.primary.database}")
    private String database;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory());
    }

    @Bean
    @Primary
    public MongoDbFactory primaryFactory() throws Exception {
        return new SimpleMongoDbFactory(
                new MongoClient(host, port), database
        );
    }

}
