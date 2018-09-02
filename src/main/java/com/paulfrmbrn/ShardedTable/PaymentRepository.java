package com.paulfrmbrn.ShardedTable;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


// todo test
// todo javadoc
@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String>, PaymentRepositoryCustom {

    Flux<Payment> findByPayerId(long payerId);

}
