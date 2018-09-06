package com.paulfrmbrn.sharded.table.primary;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.paulfrmbrn.sharded.table", mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig {

}
