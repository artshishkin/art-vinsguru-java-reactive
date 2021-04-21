package net.shyshkin.study.reactive.Section10CombiningPublishers;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

public class Lec115_Concat_Test {

    @Test
    void concat() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<Integer> flux1 = Flux.range(1, 5);
        Flux<Integer> flux2 = Flux.range(10, 5);

        //when
        Flux<Integer> finalFlux = flux2.concatWith(flux1);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void concat_factory() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<Integer> flux1 = Flux.range(1, 5);
        Flux<Integer> flux2 = Flux.range(10, 6);
        Flux<Integer> flux3 = Flux.range(20, 7);

        //when
        Flux<Integer> finalFlux = Flux.concat(flux1, flux2, flux3);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void concat_error() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        Flux<Integer> flux1 = Flux.range(1, 5);
        Flux<Integer> flux2 = Flux.error(new RuntimeException("Fake Error"));
        Flux<Integer> flux3 = Flux.range(20, 7);

        //when
        Flux<Integer> finalFlux = Flux.concat(flux1, flux2, flux3);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void concat_delayError() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        Flux<Integer> flux1 = Flux.range(1, 5);
        Flux<Integer> flux2 = Flux.error(new RuntimeException("Fake Error"));
        Flux<Integer> flux3 = Flux.range(20, 7);

        //when
        Flux<Integer> finalFlux = Flux.concatDelayError(flux1, flux2, flux3);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }
}
