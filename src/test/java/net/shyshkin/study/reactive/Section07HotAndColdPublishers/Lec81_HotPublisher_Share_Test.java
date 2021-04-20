package net.shyshkin.study.reactive.Section07HotAndColdPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class Lec81_HotPublisher_Share_Test {

    @Test
    void hotPublisher_share() throws InterruptedException {
        //given
        log.debug("Start test");
        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .share();

        //when
        Util.sleep(2);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(0.4);
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await();
    }

    @Test
    void hotPublisher_share_stepVerifier() throws InterruptedException {
        //given
        log.debug("Start test");
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100))
                .share();

        //when
        Util.sleep(0.1);
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(0.4);

        //then - Sub 2
        StepVerifier.create(movieStream)
                .thenConsumeWhile(s -> true, s -> counter.incrementAndGet())
                .verifyComplete();
        log.debug("Sub2 received {} scenes", counter.get());
        assertTrue(counter.get() < 7, "Second subscriber should receive less scenes");
        latch.await();
    }

    //movie-theatre
    private Stream<String> getMovie() {
        log.debug("Got the movie streaming request");
        return IntStream.rangeClosed(1, 7)
                .mapToObj(i -> String.format("Scene %2d", i));
    }
}
