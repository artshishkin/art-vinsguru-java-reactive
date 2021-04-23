package net.shyshkin.study.reactive.Section11Batching.assignment2;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class KidsProcessService implements ProcessService {

    @Override
    public Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> process() {
        return orderFlux -> orderFlux
                .doOnNext(discount())
                .flatMap(addFreeProduct())
                .doOnComplete(() -> log.debug("finished process"));
    }

    private Consumer<PurchaseOrder> discount() {
        return order -> order.setPrice(order.getPrice() * 0.5);
    }

    private Function<PurchaseOrder, Flux<PurchaseOrder>> addFreeProduct() {
        return order -> Flux.just(order, freeProductGenerate());
    }

    private PurchaseOrder freeProductGenerate() {
        PurchaseOrder freeOrder = new PurchaseOrder();
        freeOrder.setItem("FREE - " + freeOrder.getItem());
        freeOrder.setPrice(0d);
        freeOrder.setQuantity(1);
        return freeOrder;
    }
}
