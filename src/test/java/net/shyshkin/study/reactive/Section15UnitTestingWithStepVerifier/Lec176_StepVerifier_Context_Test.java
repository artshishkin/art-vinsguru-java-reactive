package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThat;

public class Lec176_StepVerifier_Context_Test {

    @Test
    void context_error() {

        StepVerifier.create(getWelcomeMessage())
                .verifyErrorSatisfies(ex -> assertThat(ex).isInstanceOf(RuntimeException.class).hasMessage("Unauthenticated"));

    }

    @Test
    void context_correct() {

        StepVerifierOptions options = StepVerifierOptions.create().withInitialContext(Context.of("user", "art"));

        StepVerifier
                .create(getWelcomeMessage(), options)
                .expectNext("Welcome art")
                .verifyComplete();

    }

    private Mono<String> getWelcomeMessage() {
        return Mono
                .deferContextual(contextView -> Mono.just(
                        contextView
                                .getOrEmpty("user")
                                .map(user -> "Welcome " + user)
                                .orElseThrow(() -> new RuntimeException("Unauthenticated"))));
    }
}
