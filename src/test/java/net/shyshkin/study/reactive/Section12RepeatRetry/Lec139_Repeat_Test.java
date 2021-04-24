package net.shyshkin.study.reactive.Section12RepeatRetry;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Lec139_Repeat_Test {

    @Test
    void repeat() {
        //given
        sourceFlux()

                //when
                .repeat(2)

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void repeatMemory() {
        //given
        memoryFlux()

                //when
                .repeat(2)

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void repeatRandom() {
        //given
        randomFlux()

                //when
                .repeat(2)

                //then
                .subscribe(Util.subscriber());
    }

    private Flux<Integer> sourceFlux() {
        return Flux.range(1, 3)
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"));
    }

    AtomicInteger memory = new AtomicInteger(1);

    private Flux<Integer> memoryFlux() {
        return Flux.range(1, 3)
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("source flux - onCompleted"))
                .map(i -> memory.getAndIncrement());
    }

    private Flux<Integer> randomFlux() {
        return Flux.range(1, 3)
                .map(i -> Util.FAKER.random().nextInt(1, 100))
                .doOnSubscribe(subs -> log.debug("Subscribed"))
                .doOnComplete(() -> log.debug("random flux - onCompleted"));
    }

}
