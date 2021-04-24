package net.shyshkin.study.reactive.Section12RepeatRetry;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Lec140_Repeat_WithCondition_Test {

    @Test
    void repeatForever() {
        //given
        memoryFlux()

                //when
                .repeat()

                //then
                .take(Duration.ofSeconds(1))
                .subscribe(Util.subscriber());
    }

    @Test
    void repeatWithError() {
        //given
        fluxWithError()

                //when
                .repeat()

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void repeatWithCondition() {
        //given
        memoryFlux()

                //when
                .repeat(() -> memory.get() < 14)

                //then
                .subscribe(Util.subscriber());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 20})
    void repeatWithConditionAndCount(int numRepeat) {
        //given
        memoryFlux()

                //when
                .repeat(numRepeat, () -> memory.get() < 14)

                //then
                .subscribe(Util.subscriber());
    }

    AtomicInteger memory = new AtomicInteger(1);

    private Flux<Integer> memoryFlux() {
        return Flux.range(1, 3)
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> memory.getAndIncrement());
    }

    private Flux<Integer> fluxWithError() {
        return Flux.range(1, 3)
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> memory.getAndIncrement())
                .map(i -> i / (i - 5));
    }
}
