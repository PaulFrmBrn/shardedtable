package com.paulfrmbrn.ShardedTable.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BigDecimalToDoubleConverter implements Converter<BigDecimal, Double> {

    @Override
    public Double convert(BigDecimal source) {
        System.out.println("source = " + source);
        return source.doubleValue();
    }
}