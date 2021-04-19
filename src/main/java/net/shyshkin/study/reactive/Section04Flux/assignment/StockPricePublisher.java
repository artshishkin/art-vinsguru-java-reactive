package net.shyshkin.study.reactive.Section04Flux.assignment;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class StockPricePublisher {

    private static final int DELTA_MAX = 5;

    public static Flux<Integer> getPrice() {

        AtomicReference<Integer> price = new AtomicReference<>(100);

        return Flux.interval(Duration.ofMillis(50))
                .map(i -> delta())
                .map(delta -> price.updateAndGet(v -> v + delta));
    }

    private static int delta() {
        return ThreadLocalRandom.current().nextInt(-DELTA_MAX, DELTA_MAX + 1);
    }
}
