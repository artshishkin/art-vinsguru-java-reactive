package net.shyshkin.study.reactive.Section11Batching;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec123_Batching_WithBuffer_Test {

    @Test
    void allItems() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        eventStream()

                //then
                .take(12)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void usingBuffer_fluxOfLists() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStream()

                //then
                .take(12)
                .buffer(5)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void usingBuffer_flux() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStream()

                //then
                .take(12)
                .buffer(5)
                .flatMap(Flux::fromIterable)
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void bufferedTimespan() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStream()

                //then
                .take(12)
                .buffer(Duration.ofMillis(1100))
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void bufferTimeout_randomInterval() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStreamRandomTime()

                //then
                .take(120)
                .bufferTimeout(5, Duration.ofMillis(500))
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void bufferTimeout_slowFast() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStreamSlowFast()

                //then
                .take(30)
                .bufferTimeout(5, Duration.ofMillis(500))
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    private Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                .map(i -> String.format("event %03d", i));
    }

    private Flux<String> eventStreamRandomTime() {
        return Flux.generate(() -> 0, (tick, sink) -> {
            sink.next(String.format("event %03d", tick));
            Util.sleep(0.001 * Util.FAKER.random().nextInt(10, 300));
            return tick + 1;
        });
    }

    private Flux<String> eventStreamSlowFast() {
        return Flux
                .concat(
                        Flux.interval(Duration.ZERO, Duration.ofMillis(300)).take(8),
                        Flux.interval(Duration.ofMillis(1100)).take(3).map(i -> i + 10),
                        Flux.interval(Duration.ZERO, Duration.ofMillis(30)).map(i -> i + 20)
                )
                .map(i -> String.format("event %03d", i));
    }

}
