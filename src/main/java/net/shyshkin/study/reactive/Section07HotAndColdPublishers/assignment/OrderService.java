package net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class OrderService {

    private Flux<PurchaseOrder> flux;

    public Flux<PurchaseOrder> orderStream() {
        if (flux == null) flux = getOrderStream();
        return flux;
    }

    private Flux<PurchaseOrder> getOrderStream() {
        return Flux.interval(Duration.ofMillis(100))
                .map(i -> new PurchaseOrder())
                .publish()
                .refCount(2);
    }
}
