package com.paulfrmbrn.sharded.table.dao;

import com.paulfrmbrn.sharded.table.dao.primary.PrimaryRepository;
import com.paulfrmbrn.sharded.table.dao.secondary.SecondaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public class CommonRepository {

    @Value("${shard.key.range.splitters}")
    private Set<Integer> keyRangeSplitters;

    @Autowired
    private PrimaryRepository primaryRepository;

    @Autowired
    private SecondaryRepository secondaryRepository;

    public CommonRepository() {

    }


//    public void save(Payment payment) {
//        getShard(payment.getStoreId()).save(payment);
//    }

//    private MongoRepository getShard(long storeId) {
//        if ()
//    }


}
