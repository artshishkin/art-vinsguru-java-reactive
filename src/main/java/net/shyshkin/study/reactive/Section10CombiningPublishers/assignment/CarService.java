package net.shyshkin.study.reactive.Section10CombiningPublishers.assignment;

import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class CarService {

    public Flux<Integer> getPrice() {
        return Flux.combineLatest(monthStream(), demandStream(), (month, demand) -> (int) ((10000 - month * 100) * demand));
    }

    private Flux<Double> demandStream() {
        return Flux.interval(Duration.ofSeconds(3))
                .map(i -> Util.FAKER.random().nextInt(80, 120) / 100d)
                .startWith(1d);
    }

    private Flux<Long> monthStream() {
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(1));
    }
}
