package com.paulfrmbrn.sharded.table;

import java.math.BigDecimal;

public class Summary {

    private final long id;
    private final BigDecimal total;

    public Summary(long id, BigDecimal total) {
        this.id = id;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "id=" + id +
                ", total=" + total +
                '}';
    }
}
