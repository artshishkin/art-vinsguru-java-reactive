package net.shyshkin.study.reactive.Section12RepeatRetry;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec143_Retry_Spec_Test {


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
    void retry() {
        //given
        orderService(Util.FAKER.business().creditCardNumber())

                //when
                .doOnError(Util.onError)
                .retry(5)

                //then
                .subscribe(Util.subscriber(latch));

    }

    @RepeatedTest(5)
    void retryAdvanced() {
        //given
        orderService(Util.FAKER.business().creditCardNumber())

                //when
                .doOnError(Util.onError)
                .retryWhen(Retry.from(retrySignalFlux -> {

                    return retrySignalFlux
                            .doOnNext(rs -> log.debug("{}:{}:{}:{}:{}",
                                    rs.totalRetriesInARow(),
                                    rs.totalRetries(),
                                    rs.failure().getClass().getName(),
                                    rs.failure().getMessage(),
                                    rs.retryContextView().size()
                            ))
                            .handle((rs, synchronousSink) -> {
                                if ("500".equals(rs.failure().getMessage())) {

                                    if (rs.totalRetriesInARow() > 3) {
                                        synchronousSink.error(
                                                Exceptions.retryExhausted(
                                                        rs.totalRetriesInARow() + "/4 in a row (" + rs.totalRetries() + " total)",
                                                        rs.failure())
                                        );
                                    } else
                                        synchronousSink.next(1);
                                } else if ("404".equals(rs.failure().getMessage())) {
                                    synchronousSink.error(rs.failure());
                                }
                            })
                            .delayElements(Duration.ofMillis(100));
                }))

                //then
                .take(Duration.ofSeconds(5))
                .subscribe(Util.subscriber(latch));

    }

    //order service
    private Mono<String> orderService(String ccNumber) {
        return Mono.fromSupplier(() -> {
            processPayment(ccNumber);
            return Util.FAKER.idNumber().valid();
        });
    }

    //payment service
    private void processPayment(String ccNumber) {
        Integer random = Util.FAKER.random().nextInt(1, 10);
        if (random < 8)
            throw new RuntimeException("500");
        else if (random < 10)
            throw new RuntimeException("404");
        log.debug("Successful payment `{}`", ccNumber);
    }
}
