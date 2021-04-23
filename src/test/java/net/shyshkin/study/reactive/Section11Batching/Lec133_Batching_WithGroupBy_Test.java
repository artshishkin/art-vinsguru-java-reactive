package net.shyshkin.study.reactive.Section11Batching;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec133_Batching_WithGroupBy_Test {

    @Test
    void groupBy() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Starting...");

        //when
        eventStream()
                .delayElements(Duration.ofMillis(100))
                .groupBy(i -> i % 2)

                //then
                .subscribe(gf -> process(gf, gf.key()), Util.onError, latch::countDown);

        latch.await();
    }

    private void process(Flux<Integer> flux, int key) {
        log.debug("Process started for key: {}", key);
        flux.subscribe(
                item -> log.debug("key: {}, item: {}", key, item),
                Util.onError,
                () -> log.debug("batch for key {} saved\n----------------------------", key)
        );
    }

    private Flux<Integer> eventStream() {
        return Flux.concat(
                Flux.range(1, 5).map(i -> i * 2),
                Flux.range(11, 25)
        );
    }
}
