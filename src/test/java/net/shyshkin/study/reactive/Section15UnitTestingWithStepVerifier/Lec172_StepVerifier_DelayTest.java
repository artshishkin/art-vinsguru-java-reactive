package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import net.shyshkin.study.reactive.Section11Batching.assignment.BookOrder;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lec172_StepVerifier_DelayTest {


    @Test
    void assertNext() {
        //when
        Mono<BookOrder> mono = Mono.fromSupplier(() -> BookOrder.builder().author("foo").build());

        //then
        StepVerifier.create(mono)
                .assertNext(order -> assertNotNull(order.getAuthor()))
                .verifyComplete();
    }

    @Test
    void assertNext_delay() {
        //when
        Mono<BookOrder> mono = Mono
                .fromSupplier(() -> BookOrder.builder().author("foo").build())
                .delayElement(Duration.ofMillis(150));

        //then
        StepVerifier.create(mono)
                .assertNext(order -> assertNotNull(order.getAuthor()))
                .expectComplete()
                .verify(Duration.ofMillis(170));
    }


}
