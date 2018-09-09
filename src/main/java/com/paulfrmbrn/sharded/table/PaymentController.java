package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.dao.ShardedRepository;
import com.paulfrmbrn.sharded.table.dao.primary.PrimaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;


// todo test
// todo javadoc
// todo functional ednpoints
@RestController
public class PaymentController {

    @Autowired
    private PrimaryRepository primaryRepository;

    @Autowired
    private ShardedRepository shardedRepository;

    @GetMapping("/payments")
    public Flux<Payment> listPayment() {
        return Flux.fromIterable(primaryRepository.findAll());
    }

    @GetMapping("/payer/total/{id}")
    public BigDecimal getPayerTotal(@PathVariable Long id) {
        //return Mono.empty();
        return shardedRepository.getPayerTotal(id);
    }


}
