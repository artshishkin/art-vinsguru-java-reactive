package net.shyshkin.study.reactive.Section04Flux.assignment;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

class StockPriceObserverTest {

    @Test
    void observe() throws InterruptedException {
        //given
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Integer> flux = StockPricePublisher.getPrice();

        //when - then
        Subscriber<? super Integer> subscriber = new StockPriceObserver(countDownLatch);

        flux.subscribe(subscriber);
        countDownLatch.await();
    }
}