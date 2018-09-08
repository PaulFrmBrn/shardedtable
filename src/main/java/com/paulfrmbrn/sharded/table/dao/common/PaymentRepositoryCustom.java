package com.paulfrmbrn.sharded.table.dao.common;

import java.math.BigDecimal;
import java.util.Optional;

// todo test
// todo javadoc
// todo rename
public interface PaymentRepositoryCustom {

    Optional<BigDecimal> getTotalForPayer(long payerId);

}
