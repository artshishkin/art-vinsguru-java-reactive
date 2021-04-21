package net.shyshkin.study.reactive.Section10CombiningPublishers;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class Lec116_Merge_Test {

    @Test
    void merge() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(1));
        Flux<Integer> flux2 = Flux.range(10, 5).delayElements(Duration.ofMillis(1));

        //when
        Flux<Integer> finalFlux = flux2.mergeWith(flux1);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void merge_factory() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(1));
        Flux<Integer> flux2 = Flux.range(10, 6).delayElements(Duration.ofMillis(1));
        Flux<Integer> flux3 = Flux.range(20, 7).delayElements(Duration.ofMillis(1));

        //when
        Flux<Integer> finalFlux = Flux.merge(flux1, flux2, flux3);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void merge_airlines() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<String> flux1 = QatarFlights.getFlights();
        Flux<String> flux2 = Emirates.getFlights();
        Flux<String> flux3 = AmericanAirlines.getFlights();

        //when
        Flux<String> finalFlux = Flux.merge(flux1, flux2, flux3);

        //then
        finalFlux.subscribe(Util.subscriber(latch));
        latch.await();
    }


}
