package com.paulfrmbrn.ShardedTable.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {

    @Override
    public BigDecimal convert(Double source) {
        System.out.println("source = " + source);
        return new BigDecimal(source);
    }
}