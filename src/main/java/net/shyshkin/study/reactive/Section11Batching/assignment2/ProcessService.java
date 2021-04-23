package net.shyshkin.study.reactive.Section11Batching.assignment2;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProcessService {

    Mono<Void> process(Flux<PurchaseOrder> orderFlux);
}
