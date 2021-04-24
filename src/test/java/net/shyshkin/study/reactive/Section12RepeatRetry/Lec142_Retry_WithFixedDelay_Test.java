package net.shyshkin.study.reactive.Section12RepeatRetry;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Lec142_Retry_WithFixedDelay_Test {

    @Test
    void retryWhen_errorFlux() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        errorFlux()

                //when
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(3)))

                //then
                .subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    @DisplayName("After TOTAL 2 Retries - RetryExhaustedException was thrown")
    void retryWhen_errorSometimes() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        fluxWithError()

                //when
                .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(100)))

                //then
                .subscribe(Util.subscriber(latch));
        latch.await();
    }

    AtomicInteger memory = new AtomicInteger(1);

    private Flux<Integer> fluxWithError() {
        return Flux.range(1, 20)
                .doOnNext(i -> log.debug("initial flux produces: {}", i))
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> memory.getAndIncrement())
                .map(i -> 100 / ((i % 5) - 4))
                .doOnError(ex -> log.error("Exception was caught: {}:{}", ex.getClass().getName(), ex.getMessage()));
    }

    private Flux<Integer> errorFlux() {
        return Flux.range(1, 10)
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> i / 0)
                .doOnError(ex -> log.error("Exception was caught: {}:{}", ex.getClass().getName(), ex.getMessage()));
    }

    private Flux<Integer> randomErrorFlux() {
        return Flux.range(1, 3)
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> i / (Util.FAKER.random().nextInt(1, 5) > 3 ? 0 : 1))
                .doOnError(ex -> log.error("Exception was caught: {}:{}", ex.getClass().getName(), ex.getMessage()));
    }
}
