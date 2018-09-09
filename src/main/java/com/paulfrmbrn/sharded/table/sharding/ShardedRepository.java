package com.paulfrmbrn.sharded.table.sharding;

import com.paulfrmbrn.sharded.table.Payment;
import com.paulfrmbrn.sharded.table.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShardedRepository {

    private static final Logger logger = LoggerFactory.getLogger(ShardedRepository.class);

    private final ShardingService shardingService;

    public ShardedRepository(ShardingService shardingService) {
        this.shardingService = shardingService;
    }

    /**
     * Сохранить платеж
     */
    public Mono<Payment> save(Payment payment) {
        return shardingService.getShard(payment.getPayerId()).getMongoTemplate().save(payment);
    }

    /**
     * Выдать общую сумму потраченных средств по отправителю;
     */
    public Flux<BigDecimal> getPayerTotal(long payerId) {
        ReactiveMongoTemplate mongoTemplate = shardingService.getShard(payerId).getMongoTemplate();
        return PaymentRepositoryUtils.getPayerTotal(payerId, mongoTemplate);
    }

    /**
     * Получить TOP-N отправителей (максимально много заплативших);
     */
    public Flux<Summary> getTopPayers(int number) {

        return Flux.fromStream(shardingService.getAllShards().stream())
                .map(Shard::getMongoTemplate)
                .flatMap(template -> PaymentRepositoryUtils.getTopPayers(number, template))
                .sort(Comparator.comparing(Summary::getTotal).reversed())
                .take(number);
    }

    /**
     * Получить TOP-N по магазинам (максимально много заработавших).
     */
    public Flux<Summary> getTopStores(int number) {

        Flux<Summary> summaryFlux = Flux.just();

        Flux.fromStream(shardingService.getAllShards().stream())
                .map(Shard::getMongoTemplate)
                .flatMap(PaymentRepositoryUtils::getStoresTotal)
                .collectList()
                .subscribe(summaries -> {

                    if (logger.isDebugEnabled()) {
                        logger.debug("summaries: {}", summaries);
                    }

                    Map<Long, BigDecimal> map = summaries.stream()
                            .collect(Collectors.toMap(
                                    Summary::getId,
                                    Summary::getTotal,
                                    BigDecimal::add));

                    if (logger.isDebugEnabled()) {
                        logger.debug("all shard summary: {}", map);
                    }

                    List<Summary> result = map.entrySet().stream()
                            .sorted((Map.Entry.<Long, BigDecimal>comparingByValue().reversed()))
                            .limit(number)
                            .map(it -> new Summary(it.getKey(), it.getValue()))
                            .collect(Collectors.toList());

                    if (logger.isDebugEnabled()) {
                        logger.debug("summary: {}", result);
                    }

                    summaryFlux.concatWith(Flux.fromIterable(result));
                });

        return summaryFlux;
    }

    /**
     * Получить чписок всех платежей
     */
    public Flux<Payment> listPayments() {
        return Flux.fromStream(shardingService.getAllShards().stream())
                .map(Shard::getMongoTemplate)
                .flatMap((template -> template.findAll(Payment.class)));
    }

    /**
     * Загрузить платежи из файла в шарды
     */
    public void logPayments() {
        logger.debug("Payments:");
        logger.debug("----------------------------->>");
        shardingService.getAllShards().forEach(shard -> {
            logger.debug("shard {} ------>>", shard.getNumber());
            shard.getMongoTemplate().findAll(Payment.class).subscribe(it -> logger.info(it.toString()));
        });
        logger.debug("-----------------------------<<");
    }

    /**
     * Удалить все платежи из шардов
     */
    public void deleteAll() {
        logger.debug("Deleting all Payments");
        shardingService.getAllShards().forEach(it -> it.getMongoTemplate().remove(Payment.class).all().subscribe());
        logger.debug("All Payments are deleted");
    }

}
