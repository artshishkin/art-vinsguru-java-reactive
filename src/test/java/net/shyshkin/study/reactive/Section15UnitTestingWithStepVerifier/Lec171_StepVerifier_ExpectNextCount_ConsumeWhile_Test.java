package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec171_StepVerifier_ExpectNextCount_ConsumeWhile_Test {


    @Test
    void expectNextCount() {
        //when
        Flux<Integer> flux = Flux.range(1, 50);

        //then
        StepVerifier.create(flux)
                .expectNextCount(50)
                .verifyComplete();
    }

    @Test
    void consumeWhile() {
        //when
        Flux<Integer> flux = Flux.range(1, 50);

        //then
        StepVerifier.create(flux)
                .thenConsumeWhile(i -> i < 100)
                .verifyComplete();
    }

}
