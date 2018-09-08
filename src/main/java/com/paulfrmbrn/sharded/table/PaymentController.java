package com.paulfrmbrn.sharded.table;

import com.paulfrmbrn.sharded.table.dao.primary.PrimaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Optional;


// todo test
// todo javadoc
// todo functional ednpoints
@RestController
public class PaymentController {

    @Autowired
    private PrimaryRepository primaryRepository;

    @GetMapping("/payments")
    public Flux<Payment> listPayment() {
        return Flux.fromIterable(primaryRepository.findAll());
    }

    @GetMapping("/payer/total/{id}")
    public Optional<BigDecimal> getPayerTotal(@PathVariable Long id) {
        //return Mono.empty();
        return primaryRepository.getTotalForPayer(id);
    }


}
