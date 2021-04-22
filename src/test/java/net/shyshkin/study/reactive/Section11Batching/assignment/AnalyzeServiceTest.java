package net.shyshkin.study.reactive.Section11Batching.assignment;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
class AnalyzeServiceTest {

    private AnalyzeService analyzeService;
    private BookOrderService orderService;
    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        analyzeService = new AnalyzeService();
        orderService = new BookOrderService();

        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void revenueAnalyze() {
        //given

        Flux<BookOrder> orders = orderService.getOrders();

        //when
        Flux<RevenueReport> revenue = analyzeService.revenue(orders, requestedGenres());

        //then
        revenue
                .take(Duration.ofSeconds(15))
                .subscribe(Util.subscriber(latch));
    }

    private Flux<List<String>> requestedGenres() {

        return Flux
                .concat(
                        Flux.just("Science fiction", "Fantasy", "Suspense/Thriller"),
                        Flux.just("Fantasy").delayElements(Duration.ofSeconds(6)),
                        Flux.just("Suspense/Thriller").delayElements(Duration.ofSeconds(5)),
                        Flux.just("Science fiction")
                )
                .bufferTimeout(5, Duration.ofSeconds(2));
    }
}