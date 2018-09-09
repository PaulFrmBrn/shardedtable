package com.paulfrmbrn.sharded.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileParser {

    private static final Logger logger = LoggerFactory.getLogger(FileParser.class);

    private final String csvFilePath;

    public FileParser(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public Stream<Payment> getPayments() {

        logger.info("csvFilePath = {}", csvFilePath);

        Stream<String> lines;
        try {
            Path path = Paths.get(csvFilePath);
            lines = Files.lines(path);
        } catch (InvalidPathException | IOException e) {
            throw new IllegalArgumentException(String.format("Invalid path: %s", csvFilePath), e);
        }
        return lines.map(FileParser::getPayment);

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


}
