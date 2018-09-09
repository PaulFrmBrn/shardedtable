package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.sharding.ShardedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShardedTableApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShardedTableApplication.class, args);
    }

    @Autowired
    ShardedRepository shardedRepository;

    @Override
    public void run(String... args) {
        this.shardedRepository.deleteAll();
    }

}
