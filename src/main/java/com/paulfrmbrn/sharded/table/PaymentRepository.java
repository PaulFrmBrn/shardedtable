package com.paulfrmbrn.sharded.table;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


// todo test
// todo javadoc
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String>, PaymentRepositoryCustom {

    Flux<Payment> findByPayerId(long payerId);

}
