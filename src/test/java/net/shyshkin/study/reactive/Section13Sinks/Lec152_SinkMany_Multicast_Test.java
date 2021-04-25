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
public class Lec152_SinkMany_Multicast_Test {

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
    void multicast() {
        //given
        Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art", latch));
        IntStream.rangeClosed(1, 5).forEach(sink::tryEmitNext);
        sink.tryEmitComplete();
    }

    @Test
    void multicast_multipleSubscribers() {
        //given
        Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art", latch));
        flux.subscribe(Util.subscriber("kate", latch));
        IntStream.rangeClosed(1, 5).forEach(sink::tryEmitNext);
        sink.tryEmitComplete();
    }

    @Test
    void multicast_buffer() {
        //given
        Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        IntStream.rangeClosed(1, 3).forEach(sink::tryEmitNext);
        flux.subscribe(Util.subscriber("art", latch));
        flux.subscribe(Util.subscriber("kate", latch));
        IntStream.rangeClosed(4, 5).forEach(sink::tryEmitNext);
        flux.subscribe(Util.subscriber("arina", latch));
        sink.tryEmitNext("second and next subscribers receive messages after subscription");
        sink.tryEmitComplete();
        log.debug("First subscriber receives all the buffer, second - only after subscription");
    }
}
