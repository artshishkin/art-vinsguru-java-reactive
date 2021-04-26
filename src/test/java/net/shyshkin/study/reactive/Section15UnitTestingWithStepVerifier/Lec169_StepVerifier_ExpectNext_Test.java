package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec169_StepVerifier_ExpectNext_Test {

    @Test
    void expectNext() {

        //when
        Flux<Integer> flux = Flux.just(1, 2, 3);

        //then
        StepVerifier.create(flux)
                .expectNext(1)
                .expectNext(2, 3)
                .verifyComplete();
    }
}
