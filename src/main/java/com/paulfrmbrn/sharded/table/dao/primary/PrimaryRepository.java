package com.paulfrmbrn.sharded.table.dao.primary;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.dao.common.PaymentRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrimaryRepository extends MongoRepository<Payment, String>, PaymentRepositoryCustom {
}