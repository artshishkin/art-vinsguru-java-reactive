package net.shyshkin.study.reactive.Section13Sinks;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@Slf4j
public class Lec156_SinkMany_Replay_Test {

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(2);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void replay() throws InterruptedException {
        //given
        Sinks.Many<Object> sink = Sinks.many().replay().all();
        System.setProperty("reactor.bufferSize.small", "16");

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art", latch));
        flux
                .delayElements(Duration.ofMillis(100))
                .subscribe(Util.subscriber("kate", latch));
        IntStream.rangeClosed(1, 100).forEach(sink::tryEmitNext);
        sink.tryEmitComplete();

        latch.await();

        log.debug("kate is slow but got all 100, art is fast and got all 100 items");

    }

    @Test
    void replay_all() {
        //given
        Sinks.Many<Object> sink = Sinks.many().replay().all();

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        IntStream.rangeClosed(1, 3).forEach(sink::tryEmitNext);
        flux.subscribe(Util.subscriber("art", latch));
        flux.subscribe(Util.subscriber("kate", latch));
        IntStream.rangeClosed(4, 5).forEach(sink::tryEmitNext);
        flux.subscribe(Util.subscriber("arina", latch));
        sink.tryEmitNext("second and next subscribers receive all the messages");
        sink.tryEmitNext("all elements pushed to this sink are replayed to new subscribers");
        flux.subscribe(Util.subscriber("nazar", latch));
        sink.tryEmitComplete();
    }


    @Test
    void replay_last10_forNewSubscribers() throws InterruptedException {
        //given
        Sinks.Many<Object> sink = Sinks.many().replay().limit(10);
        System.setProperty("reactor.bufferSize.small", "16");
        latch = new CountDownLatch(3);

        //when
        Flux<Object> flux = sink.asFlux();

        //then
        flux.subscribe(Util.subscriber("art", latch));
        flux
                .delayElements(Duration.ofMillis(50))
                .subscribe(Util.subscriber("kate", latch));
        IntStream.rangeClosed(1, 100).forEach(sink::tryEmitNext);

        Util.sleep(0.2);
        flux.subscribe(Util.subscriber("arina", latch));

        sink.tryEmitNext("new subscribers receive up to 10 elements, old subscribers receive all");
        sink.tryEmitComplete();
    }

}
