package net.shyshkin.study.reactive.Section13Sinks;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@Slf4j
public class Lec149_SinkMany_Unicast_Test {

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void unicast() {
        //given
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art", latch));
        IntStream.rangeClosed(1, 5).forEach(sink::tryEmitNext);
        sink.tryEmitComplete();
    }
}
