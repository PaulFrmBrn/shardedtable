package com.paulfrmbrn.sharded.table;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class BigDecimalFormat {

    // DecimalFormat is not thread safe
    private static final ThreadLocal<DecimalFormat> FORMATTER =
            ThreadLocal.withInitial(() -> {
                DecimalFormat format = new DecimalFormat("#,###");
                format.setParseBigDecimal(true);
                return format;
            });

    public static BigDecimal format(String value) {
        try {
            return (BigDecimal) FORMATTER.get().parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Error occurred while parsing string: %s", value), e);
        }
    }


}
