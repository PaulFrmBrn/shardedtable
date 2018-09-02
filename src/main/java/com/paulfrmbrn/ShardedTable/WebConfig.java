package com.paulfrmbrn.ShardedTable;

import com.paulfrmbrn.ShardedTable.converters.BigDecimalToDoubleConverter;
import com.paulfrmbrn.ShardedTable.converters.DoubleToBigDecimalConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BigDecimalToDoubleConverter());
        registry.addConverter(new DoubleToBigDecimalConverter());
    }
}