package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class Lec33_Flux_MultipleSubscribers_Test {

    @Test
    void multiple_simple() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);

        //when - then
        flux.subscribe(i -> log.debug("Sub 1: {}", i));
        flux.subscribe(i -> log.debug("Sub 2: {}", i));
    }

    @Test
    void multiple_mixed() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);

        //when - then
        flux
                .delayElements(Duration.ofMillis(20))
                .subscribe(i -> log.debug("Sub 1: {}", i), null, () -> log.debug("Sub 1: Completed"));
        Util.sleep(0.01);
        flux
                .delayElements(Duration.ofMillis(20))
                .subscribe(i -> log.debug("Sub 2: {}", i), null, () -> log.debug("Sub 2: Completed"));

        Util.sleep(1);
    }

    @Test
    void multiple_modified() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6);
        Flux<Integer> evenFlux = flux.filter(i -> i % 2 == 0);

        //when - then
        flux
                .subscribe(i -> log.debug("Sub 1: {}", i), null, () -> log.debug("Sub 1: Completed"));
        evenFlux
                .subscribe(i -> log.debug("Sub 2: {}", i), null, () -> log.debug("Sub 2: Completed"));
    }
}
