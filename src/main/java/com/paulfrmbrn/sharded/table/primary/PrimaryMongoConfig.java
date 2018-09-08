package com.paulfrmbrn.sharded.table.primary;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = PrimaryRepository.class,
        mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.primary.host}")
    private String host;

    @Value("${mongodb.primary.port}")
    private int port;

    @Value("${mongodb.primary.database}")
    private String database;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        //return new MongoTemplate(MongoClients.create("mongodb://" + host + ":" + port), database);
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}
