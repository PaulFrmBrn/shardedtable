package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.dao.ShardedRepository;
import com.paulfrmbrn.sharded.table.dao.primary.PrimaryRepository;
import com.paulfrmbrn.sharded.table.dao.secondary.SecondaryRepository;
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
import java.util.stream.BaseStream;
import java.util.stream.Stream;

@SpringBootApplication
public class ShardedTableApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ShardedTableApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShardedTableApplication.class, args);
    }


    @Value("${csv.file.path}")
    private String csvFilePath;

    @Autowired
    private PrimaryRepository primaryRepository;

    @Autowired
    private SecondaryRepository secondaryRepository;

    @Autowired
    ShardedRepository shardedRepository;


    // todo extract classes

    @Override
    public void run(String... args) {

        this.primaryRepository.deleteAll();
        this.secondaryRepository.deleteAll();

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
                .subscribe(payment -> {
                    shardedRepository.save(payment);
                });

        this.shardedRepository.save(new Payment(1, 1, BigDecimal.TEN));
        this.shardedRepository.save(new Payment(2, 2, BigDecimal.ONE));

        logger.info("Payments found with findAll():");
        logger.info("-------------------------------");
        logger.info("primary-->>>>>>>>>>>>>>");
        //primaryRepository.findAll().subscribe(it -> logger.info(it.toString()));
        primaryRepository.findAll().forEach(it -> logger.info(it.toString()));

        logger.info("secondary-->>>>>>>>>>>>>>");
        //secondaryRepository.findAll().subscribe(it -> logger.info(it.toString()));
        secondaryRepository.findAll().forEach(it -> logger.info(it.toString()));
        logger.info("-------------------------------");

//        logger.info("************************************************************");
//        logger.info("Start printing mongo objects");
//        logger.info("************************************************************");
//
//        this.primaryRepository.save(new Payment(1, 1, BigDecimal.TEN));
//
//        this.secondaryRepository.save(new Payment(2, 2, BigDecimal.ONE));
//
//        List<Payment> primaries = this.primaryRepository.findAll();
//        for (Payment  primary : primaries) {
//            logger.info(primary.toString());
//        }
//
//        List<Payment> secondaries = this.secondaryRepository.findAll();
//        for (Payment secondary : secondaries) {
//            logger.info(secondary.toString());
//        }
//
//        logger.info("************************************************************");
//        logger.info("Ended printing mongo objects");
//        logger.info("************************************************************");



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

        BigDecimal amount = BigDecimalFormat.format(tokens[2]);

        return new Payment(payerId, storeId, amount);

    }

    private static <T> Flux<T> getFlux(Stream<T> stream) {
        return Flux.using(() -> stream, Flux::fromStream, BaseStream::close);
    }

}
