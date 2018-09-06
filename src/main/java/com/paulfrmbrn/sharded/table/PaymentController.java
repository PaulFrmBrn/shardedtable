package com.paulfrmbrn.sharded.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


// todo test
// todo javadoc
// todo functional ednpoints
@RestController
public class PaymentController {

    @Autowired
    private PaymentRepository repository;

    @GetMapping("/payments")
    public Flux<Payment> listPayment() {
        return repository.findAll();
    }

    @GetMapping("/payer/total/{id}")
    public Mono<BigDecimal> getPayerTotal(@PathVariable Long id) {
        return repository.getTotalForPayer(id);
    }


}
