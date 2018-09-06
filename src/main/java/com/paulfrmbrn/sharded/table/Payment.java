package com.paulfrmbrn.sharded.table;

import org.springframework.data.annotation.Id;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

// todo javaodc
// todo test
// todo final
public class Payment {

    @Id
    private String id;

    private long payerId;

    private long storeId;

    private BigDecimal sum;

    public Payment() {
    }

    public Payment(long payerId, long storeId, @Nonnull BigDecimal sum) {
        this.payerId = payerId;
        this.storeId = storeId;
        this.sum = requireNonNull(sum, "sum");
    }

    public String getId() {
        return id;
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

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", payerId=" + payerId +
                ", storeId=" + storeId +
                ", sum=" + sum +
                '}';
    }


}
