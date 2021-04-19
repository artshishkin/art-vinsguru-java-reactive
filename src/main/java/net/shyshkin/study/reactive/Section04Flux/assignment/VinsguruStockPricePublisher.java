package net.shyshkin.study.reactive.Section04Flux.assignment;

import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class VinsguruStockPricePublisher {

    private static final int DELTA_MAX = 5;

    public static Flux<Integer> getPrice() {

        AtomicInteger price = new AtomicInteger(100);

        return Flux
                .interval(Duration.ofMillis(50))
                .map(i -> price.getAndAccumulate(delta(), Integer::sum));
    }

    private static int delta() {
        return Util.FAKER.random().nextInt(-DELTA_MAX, DELTA_MAX);
    }
}
