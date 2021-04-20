package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Lec76_77_Operator_FlatMap_ConcatMap_Test {

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
    }

    @Test
    void flatMap() throws InterruptedException {
        //given
        UserService.getUsers()

                //when
                .map(User::getUserId)
                .flatMap(OrderService::getOrders)

                //then
                .subscribe(Util.subscriber(latch));

        latch.await(2, TimeUnit.SECONDS);
        log.debug("flatMap mixed all the Fluxes");
    }

    @Test
    void concatMap() throws InterruptedException {
        //given
        UserService.getUsers()

                //when
                .map(User::getUserId)
                .concatMap(OrderService::getOrders)

                //then
                .subscribe(Util.subscriber(latch));

        latch.await(2, TimeUnit.SECONDS);
        log.debug("concatMap concatenate Fluxes one by one");
    }
}
