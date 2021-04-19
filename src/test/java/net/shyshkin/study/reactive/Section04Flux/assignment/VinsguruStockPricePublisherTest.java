package net.shyshkin.study.reactive.Section04Flux.assignment;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

class VinsguruStockPricePublisherTest {

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void generate_take5() {
        //given
        Flux<Integer> flux = VinsguruStockPricePublisher.getPrice();

        //when
        flux.take(5)

                //then
                .subscribe(Util.onNext, Util.onError, () -> latch.countDown());
    }

    @Test
    void generate_takeUntil() {
        //given
        Flux<Integer> flux = VinsguruStockPricePublisher.getPrice();

        //when
        flux.takeUntil(price -> price < 90 || price > 110)

                //then
                .subscribe(Util.onNext, Util.onError, () -> latch.countDown());
    }

    @Test
    void generate_takeWhile() {
        //given
        Flux<Integer> flux = VinsguruStockPricePublisher.getPrice();

        //when
        flux.takeWhile(price -> (price >= 90) && (price <= 110))

                //then
                .subscribe(Util.onNext, Util.onError, () -> latch.countDown());
    }
}