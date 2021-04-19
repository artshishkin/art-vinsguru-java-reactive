package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Lec56_57_FluxGenerate_HowToHaveCounter_useWithState_Test {

    @Test
    void counter_badSolution() {
        //given
        AtomicInteger counter = new AtomicInteger(0);

        Flux.generate(
                synchronousSink -> {
                    String name = Util.FAKER.country().name();
                    synchronousSink.next(name);
                    int count = counter.incrementAndGet();
                    log.debug("Emitting [{}][{}] {}", Thread.currentThread().getName(), count, name);
                    if ("canada".equals(name.toLowerCase()) || count >= 10)
                        synchronousSink.complete();
                })
                //when - then
                .subscribe(Util.subscriber());

        counter.incrementAndGet();
        log.debug("Counter is declared outside the SynchronousSink -> we can affect loop from outside");

    }

    @Test
    void counter_correctSolution() {
        //given

        Flux.generate(
                () -> 1,
                (counter, synchronousSink) -> {
                    String name = Util.FAKER.country().name();
                    synchronousSink.next(name);
                    log.debug("Emitting [{}][{}] {}", Thread.currentThread().getName(), counter, name);
                    if ("canada".equals(name.toLowerCase()) || counter >= 10)
                        synchronousSink.complete();
                    return counter + 1;
                })
                //when - then
                .subscribe(Util.subscriber());
    }
}
