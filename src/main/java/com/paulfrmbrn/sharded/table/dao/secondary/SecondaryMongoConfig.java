package com.paulfrmbrn.sharded.table.dao.secondary;

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
public class SecondaryMongoConfig /*extends AbstractMongoConfiguration*/ {


    @Value("${mongodb.secondary.host}")
    private String host;

    @Value("${mongodb.secondary.port}")
    private int port;

    @Value("${mongodb.secondary.database}")
    private String database;

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        //return new ReactiveMongoTemplate(MongoClients.create("mongodb://" + host + ":" + port), database);
        //return new MongoTemplate(mongoClient(), getDatabaseName());
        return new MongoTemplate(new MongoClient(host, port), database);
    }

//    @Override
//    public MongoClient mongoClient() {
//        return new MongoClient(host, port);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return database;
//    }

}
