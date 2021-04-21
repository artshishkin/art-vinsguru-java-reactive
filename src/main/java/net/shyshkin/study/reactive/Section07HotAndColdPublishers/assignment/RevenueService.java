package net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RevenueService {

    private final Map<String, Double> db = new HashMap<>(Map.of(
            "Kids", 0.0,
            "Automotive", 0.0
    ));

    public Consumer<PurchaseOrder> subscribeOrderStream() {
        return p -> db.computeIfPresent(p.getCategory(), (k, v) -> v + p.getPrice() * p.getQuantity());
    }

    public Flux<String> revenueStream() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> db.toString())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
