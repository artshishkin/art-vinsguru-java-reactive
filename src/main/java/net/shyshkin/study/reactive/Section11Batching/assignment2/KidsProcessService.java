package net.shyshkin.study.reactive.Section11Batching.assignment2;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
public class KidsProcessService implements ProcessService {

    @Override
    public Mono<Void> process(Flux<PurchaseOrder> orderFlux) {
        return orderFlux
                .doOnNext(order -> log.debug("received: {}", order))
                .doOnNext(discount())
                .doOnNext(oneFreeProduct())
                .doOnNext(order -> log.debug("packaging: {}", order))
                .doOnComplete(() -> log.debug("finished process"))
                .then();
    }

    private Consumer<PurchaseOrder> discount() {
        return order -> order.setPrice(order.getPrice() * 0.5);
    }

    private Consumer<PurchaseOrder> oneFreeProduct() {
        return order -> order.setQuantity(order.getQuantity() + 1);
    }
}
