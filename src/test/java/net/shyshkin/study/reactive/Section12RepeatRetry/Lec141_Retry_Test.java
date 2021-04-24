package net.shyshkin.study.reactive.Section12RepeatRetry;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Lec141_Retry_Test {

    @Test
    void retryForever() {
        //given
        fluxWithError()

                //when
                .retry()

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void retryNTimes() {
        //given
        errorFlux()

                //when
                .retry(3)

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void retryNTimes_random() {
        //given
        randomErrorFlux()

                //when
                .retry(3)

                //then
                .subscribe(Util.subscriber());
    }

    AtomicInteger memory = new AtomicInteger(1);

    private Flux<Integer> fluxWithError() {
        return Flux.range(1, 10)
                .doOnNext(i -> log.debug("initial flux produces: {}", i))
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> memory.getAndIncrement())
                .map(i -> 100 / (i - 5))
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
