package com.paulfrmbrn.sharded.table;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

// todo test
// todo javadoc
// todo rename
interface PaymentRepositoryCustom {

    Mono<BigDecimal> getTotalForPayer(long payerId);

}
