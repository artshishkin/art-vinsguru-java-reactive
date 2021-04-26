package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class Lec170_StepVerifier_Error_Test {

    private Flux<Integer> flux;

    @BeforeEach
    void setUp() {
        flux = Flux
                .just(1, 2, 3)
                .concatWith(Flux.error(new RuntimeException("Fake exception")));
    }

    @Test
    void expectError() {
        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .verifyError();
    }

    @Test
    void expectErrorSatisfies() {
        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .expectErrorSatisfies(ex -> assertThat(ex).isInstanceOf(RuntimeException.class).hasMessage("Fake exception"))
                .verify();
    }

    @Test
    void verifyErrorSatisfies() {
        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .verifyErrorSatisfies(ex -> assertThat(ex).isInstanceOf(RuntimeException.class).hasMessage("Fake exception"));
    }

    @Test
    void verifyError() {
        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .expectErrorMessage("Fake exception")
                .verify(Duration.ofMillis(1));
    }
}
