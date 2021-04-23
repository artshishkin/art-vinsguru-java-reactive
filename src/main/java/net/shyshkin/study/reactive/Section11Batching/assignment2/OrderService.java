package net.shyshkin.study.reactive.Section11Batching.assignment2;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class OrderService {

    public Flux<PurchaseOrder> getOrders() {
        return Flux.interval(Duration.ofMillis(30))
                .map(i -> new PurchaseOrder());
    }
}
