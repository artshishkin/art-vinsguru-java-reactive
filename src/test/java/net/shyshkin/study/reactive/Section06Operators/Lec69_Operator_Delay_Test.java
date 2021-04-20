package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec69_Operator_Delay_Test {

    @Test
    void delay_request32() throws InterruptedException {
        //given
        Flux.range(1, 100)
                .log()

                //when
                .delayElements(Duration.ofMillis(100))

                //then
                .subscribe(Util.subscriber());
        log.debug("Request 32 - Queues.XS_BUFFER_SIZE = {} is set through property 'reactor.bufferSize.x'", Queues.XS_BUFFER_SIZE);
    }

    @Test
    void delay_request16() throws InterruptedException {
        //given
        System.setProperty("reactor.bufferSize.x", "16");
        Flux.range(1, 100)
                .log()

                //when
                .delayElements(Duration.ofMillis(100))

                //then
                .subscribe(Util.subscriber());
        log.debug("Request 16 - Queues.XS_BUFFER_SIZE = {} is set through property 'reactor.bufferSize.x'", Queues.XS_BUFFER_SIZE);
    }

    @Test
    void delay() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux.range(1, 100)
                .log()

                //when
                .delayElements(Duration.ofMillis(20))

                //then
                .subscribe(Util.subscriber(latch));
        latch.await();
        log.debug("Requested 32, when 75% (24) request 24 then again and again");
    }
}
