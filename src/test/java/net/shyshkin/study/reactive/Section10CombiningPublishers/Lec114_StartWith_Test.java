package net.shyshkin.study.reactive.Section10CombiningPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class Lec114_StartWith_Test {

    @Test
    void startWith() {
        //given
        Flux<Integer> flux1 = Flux.range(1, 5);
        Flux<Integer> flux2 = Flux.range(10, 5);

        //when
        Flux<Integer> finalFlux = flux2.startWith(flux1);

        //then
        finalFlux.subscribe(Util.subscriber());
    }


    @Test
    void startWith_name() {
        //given
        NameGenerator generator = new NameGenerator();

        //when
        generator.generateNames()
                .take(5)
                .subscribe(Util.subscriber("sub1"));

        generator.generateNames()
                .take(4)
                .subscribe(Util.subscriber("sub2"));

        //then
        Duration duration = StepVerifier.create(generator.generateNames().take(4))
                .expectNextCount(4)
                .verifyComplete();
        assertTrue(duration.compareTo(Duration.ofMillis(100)) < 0);
    }

    @Test
    void startWith_weird_condition() {
        //given
        NameGenerator generator = new NameGenerator();

        //when
        generator.generateNames()
                .filter(n -> n.startsWith("Ar"))
                .take(1)
                .subscribe(Util.subscriber("sub1"));

        //then
        generator.generateNames()
                .filter(n -> n.startsWith("A"))
                .take(2)
                .subscribe(Util.subscriber("sub2"));
    }

    @Test
    void initialValueFirst() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        log.debug("Start...");
        Flux<Integer> mainFlux = Flux
                .range(2, 4)
                .delayElements(Duration.ofMillis(1000));

        Flux<Integer> immediateFlux = mainFlux.startWith(1);

        //when
        immediateFlux
                .subscribe(Util.subscriber(latch));

        //then
        latch.await();
    }
}
