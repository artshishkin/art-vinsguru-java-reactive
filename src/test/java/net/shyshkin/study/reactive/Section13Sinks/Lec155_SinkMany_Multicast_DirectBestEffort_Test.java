package net.shyshkin.study.reactive.Section13Sinks;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@Slf4j
public class Lec155_SinkMany_Multicast_DirectBestEffort_Test {

    private CountDownLatch latch;

    @Test
    void multicast_directAllOrNothing() {
        //given
        Sinks.Many<Object> sink = Sinks.many().multicast().directAllOrNothing();
        System.setProperty("reactor.bufferSize.small", "16");
        latch = new CountDownLatch(2);

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art"));
        flux
                .delayElements(Duration.ofMillis(200))
                .subscribe(Util.subscriber("kate"));
        IntStream.rangeClosed(1, 100).forEach(sink::tryEmitNext);

        Util.sleep(7);
        log.debug("kate is slow so she got 32 items, it affects art's behaviour and he got 32 items too instead of 100");
    }

    @Test
    void multicast_directBestEffort() {
        //given
        Sinks.Many<Object> sink = Sinks.many().multicast().directBestEffort();
        System.setProperty("reactor.bufferSize.small", "16");
        latch = new CountDownLatch(2);

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art"));
        flux
                .delayElements(Duration.ofMillis(200))
                .subscribe(Util.subscriber("kate"));
        IntStream.rangeClosed(1, 100).forEach(sink::tryEmitNext);

        Util.sleep(7);
        log.debug("kate is slow so she got 32 items, art is fast and got all 100 items");
    }
}
