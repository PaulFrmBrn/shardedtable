package com.paulfrmbrn.sharded.table.primary;

import com.mongodb.MongoClient;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = PrimaryRepository.class,
        mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.primary.host}")
    private String host;

    @Value("${mongodb.primary.port}")
    private int port;

    @Value("${mongodb.primary.database}")
    private String database;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        //return new MongoTemplate(MongoClients.create("mongodb://" + host + ":" + port), database);
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

//    //@Bean
//    //@Primary
//    @Override
//    public CustomConversions customConversions() {
//        return new MongoCustomConversions(Arrays.asList(
//                //new BigDecimalDecimal128Converter(),
//                //new Decimal128BigDecimalConverter()
//                new BigDecimalToDoubleConverter(),
//                new DoubleToBigDecimalConverter()
//        ));
//    }
//
//    @Override
//    public MappingMongoConverter mappingMongoConverter() throws Exception {
//
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext());
//        converter.setCustomConversions(customConversions());
//
//        return converter;
//    }

    //@WritingConverter
    private static class BigDecimalDecimal128Converter implements Converter<BigDecimal, Decimal128> {

        @Override
        public Decimal128 convert(@NonNull BigDecimal source) {
            return new Decimal128(source);
        }
    }

    //@ReadingConverter
    private static class Decimal128BigDecimalConverter implements Converter<Decimal128, BigDecimal> {

        @Override
        public BigDecimal convert(@NonNull Decimal128 source) {
            return source.bigDecimalValue();
        }

    }

}
