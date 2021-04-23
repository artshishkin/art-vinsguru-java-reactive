package net.shyshkin.study.reactive.Section11Batching;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Lec130_Batching_WithWindow_Test {

    AtomicInteger counter = new AtomicInteger(1);

    @Test
    void usingWindow_fluxOfFluxes() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStream()

                //then
                .take(12)
                .window(5)
                .flatMap(this::saveEvents)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void usingWindow_Duration() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStream()

                //then
                .take(Duration.ofMillis(3500))
                .window(Duration.ofSeconds(1))
                .flatMap(this::saveEvents)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    @DisplayName("Timespan: slow items -> empty fluxes, fast items -> large batches")
    void windowingTimespan_slowFast() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStreamSlowFast()

                //then
                .take(Duration.ofSeconds(9))
                .window(Duration.ofSeconds(1))
                .flatMap(this::saveEvents)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    @DisplayName("WindowTimeout: slow items -> timeout margin, fast items -> size margin")
    void windowTimeout_slowFast() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStreamSlowFast()

                //then
                .take(Duration.ofSeconds(9))
                .windowTimeout(5, Duration.ofSeconds(1))
                .flatMap(this::saveEvents)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    private Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                .map(i -> String.format("event %03d", i));
    }

    private Mono<Integer> saveEvents(Flux<String> flux) {
        return flux
                .doOnNext(event -> log.debug("saving {}", event))
                .doOnComplete(() -> log.debug("batch saved\n----------------------------"))
                .then(Mono.just(counter.getAndIncrement()));
    }

    private Flux<String> eventStreamSlowFast() {
        return Flux
                .concat(
                        Flux.interval(Duration.ZERO, Duration.ofMillis(300)).take(8),
                        Flux.interval(Duration.ofMillis(2100)).take(2).map(i -> i + 10),
                        Flux.interval(Duration.ZERO, Duration.ofMillis(30)).map(i -> i + 20)
                )
                .map(i -> String.format("event %03d", i));
    }
}
