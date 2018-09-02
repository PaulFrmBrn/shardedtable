package com.paulfrmbrn.ShardedTable;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

// todo javaodc
// todo test
public class Payment {

    private final String id;

    private final long payerId;

    private final long storeId;

    private BigDecimal sum;

    public Payment(@Nonnull String id, long payerId, long storeId, @Nonnull BigDecimal sum) {
        this.id = requireNonNull(id, "id");
        this.payerId = payerId;
        this.storeId = storeId;
        this.sum = requireNonNull(sum, "sum");
    }

    public long getPayerId() {
        return payerId;
    }

    public long getStoreId() {
        return storeId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "payerId=" + payerId +
                ", storeId=" + storeId +
                ", sum=" + sum +
                '}';
    }
}
