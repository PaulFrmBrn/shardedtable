package com.paulfrmbrn.sharded.table;

import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@Document(collection = "payment2")
public class Payment {

    @Id
    private String id;

    private long payerId;

    private long storeId;

    private Decimal128 sum;

    public Payment() {
    }

    public Payment(long payerId, long storeId, @Nonnull BigDecimal sum) {
        this.payerId = payerId;
        this.storeId = storeId;
        this.sum = new Decimal128(requireNonNull(sum, "sum"));
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

    public Decimal128 getSum() {
        return sum;
    }

    public void setSum(Decimal128 sum) {
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
