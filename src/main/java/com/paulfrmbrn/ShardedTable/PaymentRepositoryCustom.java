package com.paulfrmbrn.ShardedTable;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

// todo test
// todo javadoc
// todo rename
interface PaymentRepositoryCustom {

    Mono<BigDecimal> getTotalForPayer(long payerId);

}
