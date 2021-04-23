package net.shyshkin.study.reactive.Section11Batching.assignment2;

import reactor.core.publisher.Flux;

import java.util.function.Function;

public interface ProcessService {

    Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> process();
}
