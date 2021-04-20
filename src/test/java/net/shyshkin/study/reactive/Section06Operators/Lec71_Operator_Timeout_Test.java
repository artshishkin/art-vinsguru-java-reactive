package net.shyshkin.study.reactive.Section06Operators;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec71_Operator_Timeout_Test {


    private Flux<Integer> sourceFlux;

    @BeforeEach
    void setUp() {
        sourceFlux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500));
    }

    @Test
    void timeout_TimeoutException() throws InterruptedException {
        //when
        CountDownLatch latch = new CountDownLatch(1);
        sourceFlux.timeout(Duration.ofMillis(200))

                //then
                .subscribe(Util.subscriber(latch));
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void timeout_exceptionStepVerifier() throws InterruptedException {
        //when
        Flux<Integer> timeoutFlux = sourceFlux.timeout(Duration.ofMillis(200));

        //then
        StepVerifier.create(timeoutFlux)
                .verifyErrorSatisfies(ex -> assertAll(
                        () -> assertEquals(TimeoutException.class, ex.getClass()),
                        () -> assertEquals("Did not observe any item or terminal signal within 200ms in 'concatMap' (and no fallback has been configured)", ex.getMessage())
                ));
    }

    @Test
    void timeout_fallback() throws InterruptedException {
        //when
        CountDownLatch latch = new CountDownLatch(1);
        sourceFlux.log().timeout(Duration.ofMillis(200), fallbackFlux())

                //then
                .subscribe(Util.subscriber(latch));
        latch.await(2, TimeUnit.SECONDS);
    }

    private Flux<Integer> fallbackFlux() {
        return Flux.range(-20, 10)
                .delayElements(Duration.ofMillis(100));
    }

}
