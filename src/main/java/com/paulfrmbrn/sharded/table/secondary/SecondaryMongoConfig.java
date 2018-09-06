package com.paulfrmbrn.sharded.table.secondary;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.paulfrmbrn.sharded.table", mongoTemplateRef = "secondaryMongoTemplate")
public class SecondaryMongoConfig {
}
