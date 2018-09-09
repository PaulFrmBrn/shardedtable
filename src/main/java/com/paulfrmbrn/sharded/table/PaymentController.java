package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.sharding.ShardedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

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
        return shardedRepository.listPayments();
    }

    @GetMapping("/payer/total/{id}")
    public Flux<BigDecimal> getPayerTotal(@PathVariable Long id) {
        return shardedRepository.getPayerTotal(id);
    }

    @GetMapping("/payer/top/{number}")
    public Flux<Summary> getTopPayers(@PathVariable Integer number) {
        return shardedRepository.getTopPayers(number);
    }

    @GetMapping(value = "/store/top/{number}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Summary> getTopStores(@PathVariable Integer number) {
        return shardedRepository.getTopStores(number);
    }

    @GetMapping("/payments/load")
    public void loadPayment() {
        Flux.fromStream(fileParser.getPayments())
                .flatMap(payment -> shardedRepository.save(payment))
                .subscribe();
    }


}
