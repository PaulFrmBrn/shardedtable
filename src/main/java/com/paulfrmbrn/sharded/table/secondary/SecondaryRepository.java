package com.paulfrmbrn.sharded.table.secondary;

import com.paulfrmbrn.sharded.table.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecondaryRepository extends MongoRepository<Payment, String> {
}
