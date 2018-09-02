package com.paulfrmbrn.ShardedTable;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// todo test
// todo javadoc
public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByPayerId(long payerId);

}
