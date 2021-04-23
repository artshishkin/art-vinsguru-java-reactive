package net.shyshkin.study.reactive.Section11Batching.assignment2;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class AutomotiveProcessService implements ProcessService {

    @Override
    public Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> process() {
        return orderFlux -> orderFlux
                .doOnNext(add10PercentTax())
                .doOnNext(packOrder())
                .doOnComplete(() -> log.debug("finished process"));
    }

    private Consumer<PurchaseOrder> add10PercentTax() {
        return order -> order.setPrice(order.getPrice() * 1.1);
    }

    private Consumer<PurchaseOrder> packOrder() {
        return order -> order.setItem(String.format("{{%s}}", order.getItem()));
    }
}
