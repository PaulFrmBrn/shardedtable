package com.paulfrmbrn.sharded.table.secondary;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.paulfrmbrn.sharded.table.secondary", mongoTemplateRef = "secondaryMongoTemplate")
public class SecondaryMongoConfig {


    @Value("${mongodb.secondary.host}")
    private String host;

    @Value("${mongodb.secondary.port}")
    private int port;

    @Value("${mongodb.secondary.database}")
    private String database;

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        return new MongoTemplate(secondaryFactory());
    }

    @Bean
    public MongoDbFactory secondaryFactory() throws Exception {
        return new SimpleMongoDbFactory(
                new MongoClient(host, port), database
        );
    }

}
