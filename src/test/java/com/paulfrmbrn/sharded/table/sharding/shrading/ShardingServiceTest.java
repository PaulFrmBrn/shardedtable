package com.paulfrmbrn.sharded.table.sharding.shrading;

import com.paulfrmbrn.sharded.table.sharding.Shard;
import com.paulfrmbrn.sharded.table.sharding.ShardingService;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ShardingServiceTest {

    @Test
    public void test() {

        Shard first = getShard(1);
        Shard second = getShard(2);

        ShardingService service = new ShardingService(1, 9, Arrays.asList(first, second));

        assertEquals(first, service.getShard(3));
        assertEquals(second, service.getShard(7));

    }

    private Shard getShard(int number) {
        return new Shard(number, mock(MongoRepository.class), mock(MongoTemplate.class));
    }

}