package net.shyshkin.study.reactive.Section10CombiningPublishers.assignment;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

@Slf4j
class CarServiceTest {

    CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void carService() {
        //given
        CarService carService = new CarService();

        //when
        Flux<Integer> priceFlux = carService.getPrice();

        //then
        priceFlux
                .take(14)
                .subscribe(Util.subscriber(latch));

    }
}