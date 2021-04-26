package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class Lec175_StepVerifier_ScenarioName_Test {

    @Test
    void scenarioName() {

        //given
        Flux<String> flux = Flux.just("a", "b", "c");

        //when
        StepVerifierOptions scenarioName = StepVerifierOptions.create().scenarioName("alphabets-test");

        //then
        StepVerifier
                .create(flux, scenarioName)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    void verificationStepDescription() {

        //when
        Flux<String> flux = Flux.just("a", "b", "C");

        //then
        StepVerifier
                .create(flux)
                .expectNext("a")
                .as("a-test")
                .expectNext("b")
                .as("b-test")
                .expectNext("c")
                .as("c-test")
                .verifyComplete();
    }

}
