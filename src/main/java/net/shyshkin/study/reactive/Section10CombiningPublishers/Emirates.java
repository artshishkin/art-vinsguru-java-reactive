package net.shyshkin.study.reactive.Section10CombiningPublishers;

import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Emirates {

    public static Flux<String> getFlights() {
        return Flux
                .range(1, Util.FAKER.random().nextInt(1, 10))
                .delayElements(Duration.ofMillis(Util.FAKER.random().nextInt(10, 300)))
                .map(i -> String.format("Emirates %d", Util.FAKER.random().nextInt(100, 999)))
                .filter(s -> Util.FAKER.random().nextBoolean());
    }
}
