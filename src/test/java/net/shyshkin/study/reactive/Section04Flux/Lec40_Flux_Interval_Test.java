package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec40_Flux_Interval_Test {

    @Test
    void interval_nonBlocking() {

        //given
        Flux<String> flux = Flux
                .interval(Duration.ofMillis(20))
                .log()
                .map(i -> Util.FAKER.name().name())
                .log();

        //when
        flux.take(5).subscribe(Util.onNext, Util.onError, Util.onComplete);

        //then
        log.debug("Flux.interval - asynchronous and non blocking - just exit main thread");
    }

    @Test
    void interval_stepVerifier() {

        //given
        Flux<String> flux = Flux
                .interval(Duration.ofMillis(20))
                .log()
                .map(i -> Util.FAKER.name().name())
                .log();

        //when
        StepVerifier.create(flux.take(3))

                //then
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void interval_countDownLatch() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<String> flux = Flux
                .interval(Duration.ofMillis(20))
                .log()
                .map(i -> Util.FAKER.name().name())
                .log();

        //when - then
        flux
                .take(5)
                .subscribe(Util.onNext, Util.onError, latch::countDown);
        latch.await();
    }
}
