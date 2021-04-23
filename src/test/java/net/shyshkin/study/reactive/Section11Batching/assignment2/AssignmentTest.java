package net.shyshkin.study.reactive.Section11Batching.assignment2;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

@Slf4j
class AssignmentTest {

    private OrderService orderService;
    private CountDownLatch latch;

    private Map<String, Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>>> processors;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        KidsProcessService kidsProcessService = new KidsProcessService();
        AutomotiveProcessService automotiveProcessService = new AutomotiveProcessService();
        latch = new CountDownLatch(1);
        processors = Map.of(
                "Kids", kidsProcessService.process(),
                "Automotive", automotiveProcessService.process()
        );
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void process() {
        //given
        Set<String> availableCategories = processors.keySet();

        Flux<PurchaseOrder> orderFlux = orderService
                .getOrders()
                .filter(order -> availableCategories.contains(order.getCategory()));

        //when
        orderFlux
                .take(Duration.ofSeconds(10))
                .groupBy(PurchaseOrder::getCategory)

                //then
                .flatMap(gf -> process(gf, gf.key()))
                .subscribe(Util.subscriber(latch));
    }

    private Flux<PurchaseOrder> process(Flux<PurchaseOrder> orderFlux, String category) {
        log.debug("process for {} started", category);
        return processors.get(category).apply(orderFlux);
    }

}