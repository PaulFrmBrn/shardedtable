package com.paulfrmbrn.ShardedTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

@SpringBootApplication
public class ShardedTableApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ShardedTableApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShardedTableApplication.class, args);
    }

    @Autowired
    private PaymentRepository repository;

    @Value("${csv.file.path}")
    private String csvFilePath;


    // todo extract classes

    @Override
    public void run(String... args) throws Exception {

        repository.deleteAll();

        logger.info("csvFilePath = {}", csvFilePath);

        Stream<String> lines;
        try {
            Path path = Paths.get(csvFilePath);
            lines = Files.lines(path);
        } catch (InvalidPathException | IOException e) {
            throw new IllegalArgumentException(String.format("Invalid path: %s", csvFilePath), e);
        }
        getFlux(lines)
                .map(ShardedTableApplication::getPayment)
                .subscribe(payment -> repository.save(payment));

        logger.info("Payments found with findAll():");
        logger.info("-------------------------------");
        repository.findAll().forEach(it -> logger.info(it.toString()));
        logger.info("-------------------------------");

    }

    private static Payment getPayment(String string) {

        String[] tokens = string.split(";");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Can not parse line: " + string);
        }

        long payerId;
        long storeId;
        try {
            payerId = Long.parseLong(tokens[0]);
            storeId = Long.parseLong(tokens[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Error occurred while parsing line: %s", string), e);
        }

        BigDecimal sum;
        try {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setParseBigDecimal(true);
            sum = (BigDecimal) decimalFormat.parse(tokens[2]);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Error occurred while parsing line: %s", string), e);
        }

        return new Payment(payerId, storeId, sum);

    }

    private static <T> Flux<T> getFlux(Stream<T> stream) {
        return Flux.using(() -> stream, Flux::fromStream, BaseStream::close);
    }

}
