package net.shyshkin.study.reactive.Section11Batching;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec126_Batching_WithBuffer_OverlappingAndDropping_Test {

    CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void overlap() {
        // buffer(3) = buffer(3, 3)
        //when
        eventStream()
                .take(30)
                .buffer(3, 2)

                //then
                .subscribe(Util.subscriber(latch));
    }

    @Test
    void gap() {
        //when
        eventStream()
                .take(30)
                .buffer(3, 4)
                .doOnDiscard(String.class, event -> log.debug("Discarded: {}", event))

                //then
                .subscribe(Util.subscriber(latch));
    }

    private Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(30))
                .map(i -> String.format("event %03d", i));
    }
}
