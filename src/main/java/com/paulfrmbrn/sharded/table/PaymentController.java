package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.sharding.ShardedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.Stream;


// todo test
// todo javadoc
// todo functional ednpoints
@RestController
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private ShardedRepository shardedRepository;

    @Autowired
    private FileParser fileParser;

    @GetMapping("/payments/list")
    public Flux<Payment> listPayment() {
        if (logger.isDebugEnabled()) {
            shardedRepository.logPayments();
        }
        return Flux.fromIterable(shardedRepository.listPayments());
    }

    @GetMapping("/payer/total/{id}")
    public BigDecimal getPayerTotal(@PathVariable Long id) {
        return shardedRepository.getPayerTotal(id);
    }

    @GetMapping("/payer/top/{number}")
    public List<Summary> getTopPayers(@PathVariable Integer number) {
        return shardedRepository.getTopPayers(number);
    }

    @GetMapping("/store/top/{number}")
    public List<Summary> getTopStores(@PathVariable Integer number) {
        return shardedRepository.getTopStores(number);
    }

    @GetMapping("/payments/load")
    public void loadPayment() {
        Stream<Payment> payments = fileParser.getPayments();
        getFlux(payments).subscribe(payment -> {
            shardedRepository.save(payment);
        });

    }

    private static <T> Flux<T> getFlux(Stream<T> stream) {
        return Flux.using(() -> stream, Flux::fromStream, BaseStream::close);
    }

}
