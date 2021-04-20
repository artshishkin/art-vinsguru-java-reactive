package net.shyshkin.study.reactive.Section07HotAndColdPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Lec82_HotPublisher_RefCount_Test {

    @Test
    void hotPublisher_refCount1() throws InterruptedException {
        //given
        log.debug("Start test");
        // share = publish().refCount(1)
        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .publish()
                .refCount(1);

        //when
        Util.sleep(0.1);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(0.4);
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await();
    }

    @Test
    void hotPublisher_refCount2() throws InterruptedException {
        //given
        log.debug("Start test");
        // share = publish().refCount(1)
        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .publish()
                .refCount(2);

        //when
        Util.sleep(0.1);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(0.4);
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await();
    }

    @Test
    @DisplayName("When refCount set to 7 then second subscriber should receive all the scenes")
    void hotPublisher_refCount2_stepVerifier() throws InterruptedException {
        //given
        log.debug("Start test");
        CountDownLatch latch = new CountDownLatch(1);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .publish()
                .refCount(2);

        //when
        Util.sleep(0.1);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(0.4);

        //then - Sub 2
        StepVerifier.create(movieStream)
                .expectNextCount(7)
                .verifyComplete();
        latch.await();
    }

    //movie-theatre
    private Stream<String> getMovie() {
        log.debug("Got the movie streaming request");
        return IntStream.rangeClosed(1, 7)
                .mapToObj(i -> String.format("Scene %2d", i));
    }


    @Test
    @DisplayName("When shared flux is in completed state new subscription starts emitting data from beginning")
    void hotPublisher_refCount_resubscribe() throws InterruptedException {
        //given
        log.debug("Start test");
        // share = publish().refCount(1)
        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .publish()
                .refCount(1);

        //when
        Util.sleep(0.1);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(1);
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await();
    }
}
