package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.sharding.Shard;
import com.paulfrmbrn.sharded.table.sharding.ShardedRepository;
import com.paulfrmbrn.sharded.table.sharding.ShardingService;
import com.paulfrmbrn.sharded.table.sharding.primary.PrimaryRepository;
import com.paulfrmbrn.sharded.table.sharding.secondary.SecondaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ShardedTableConfiguration {

    @Value("${shard.key.min.value}")
    private Long minKeyValue;

    @Value("${shard.key.max.value}")
    private Long maxKeyValue;

    @Value("${csv.file.path}")
    private String csvFilePath;

    @Autowired
    private PrimaryRepository primaryRepository;

    @Autowired
    private SecondaryRepository secondaryRepository;

    @Autowired
    @Qualifier("primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate;

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate;


    @Bean
    ShardingService shardingService() {
        List<Shard> shards = Arrays.asList(
                new Shard(1, primaryRepository, primaryMongoTemplate),
                new Shard(2, secondaryRepository, secondaryMongoTemplate)
        );
        return new ShardingService(minKeyValue, maxKeyValue, shards);
    }

    @Bean
    ShardedRepository commonRepository(ShardingService shardingService) {
        return new ShardedRepository(shardingService);
    }

    @Bean
    FileParser fileParser() {
        return new FileParser(csvFilePath);
    }

}

