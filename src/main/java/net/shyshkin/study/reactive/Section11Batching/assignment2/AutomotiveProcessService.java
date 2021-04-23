package net.shyshkin.study.reactive.Section11Batching.assignment2;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
public class AutomotiveProcessService implements ProcessService {
    @Override
    public Mono<Void> process(Flux<PurchaseOrder> orderFlux) {
        return orderFlux
                .doOnNext(order -> log.debug("received: {}", order))
                .doOnNext(add10PercentTax())
                .doOnNext(order -> log.debug("order after 10% tax: {}", order))
                .doOnComplete(() -> log.debug("finished process"))
                .then();
    }

    private Consumer<PurchaseOrder> add10PercentTax() {
        return order -> order.setPrice(order.getPrice() * 1.1);
    }
}
