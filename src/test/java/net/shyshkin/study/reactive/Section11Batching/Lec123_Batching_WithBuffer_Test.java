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

    private Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                .map(i -> String.format("event %03d", i));
    }

}
