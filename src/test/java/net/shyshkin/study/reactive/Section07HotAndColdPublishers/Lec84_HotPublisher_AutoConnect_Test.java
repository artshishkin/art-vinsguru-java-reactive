package net.shyshkin.study.reactive.Section07HotAndColdPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Lec84_HotPublisher_AutoConnect_Test {

    @Test
    @DisplayName("When shared flux by AUTOCONNECT is in completed state new subscription will wait and receive nothing")
    void hotPublisher_autoConnect_1() throws InterruptedException {
        //given
        log.debug("Start test");

        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .publish()
                .autoConnect(1);

        //when
        Util.sleep(0.1);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(1);
        log.debug("Sub2 is about to join stream");
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("When AUTOCONNECT set to 0 (min subscribers) then emitting is processed without subscribers")
    void hotPublisher_autoConnect_0() throws InterruptedException {
        //given
        log.debug("Start test");

        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .log()
                .publish()
                .autoConnect(0);

        //when
        Util.sleep(0.2);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(1);
        log.debug("Sub2 is about to join stream");
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await(2, TimeUnit.SECONDS);
    }

    //movie-theatre
    private Stream<String> getMovie() {
        log.debug("Got the movie streaming request");
        return IntStream.rangeClosed(1, 7)
                .mapToObj(i -> String.format("Scene %2d", i));
    }
}
