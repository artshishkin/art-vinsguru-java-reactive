package net.shyshkin.study.reactive.Section14Context;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class Lec164_Context_Demo_Test {

    @Nested
    class DefaultName {

        @Test
        void mono_simple() {

            //when
            getWelcomeMessage()

                    //then
                    .subscribe(Util.subscriber());
        }

        @Test
        void mono_withContext_present() {

            //when
            getWelcomeMessageWithContext()

                    //then
                    .contextWrite(Context.of("user", "Art"))
                    .subscribe(Util.subscriber());
        }

        @Test
        void mono_withContext_absent() {

            //when
            getWelcomeMessageWithContext()

                    //then
                    .subscribe(Util.subscriber());
        }

        private Mono<String> getWelcomeMessage() {
            return Mono.fromSupplier(() -> "Welcome");
        }

        private Mono<String> getWelcomeMessageWithContext() {
            return Mono
                    .deferContextual(contextView ->
                            Mono.fromSupplier(
                                    () -> "Welcome " + contextView
                                            .getOrDefault("user", "visitor")));
        }
    }

    @Nested
    class ErrorVinsguru {

        @Test
        void mono_withContext_present() {

            //when
            getWelcomeMessageWithContextError()

                    //then
                    .contextWrite(Context.of("user", "Art"))
                    .subscribe(Util.subscriber());
        }

        @Test
        void mono_withContext_present_stepVerifier() {

            //when
            Mono<String> mono = getWelcomeMessageWithContextError()
                    .contextWrite(Context.of("user", "Art"));

            //then
            StepVerifier.create(mono)
                    .expectNext("Welcome Art")
                    .verifyComplete();
        }

        @Test
        void mono_withContext_absent() {

            //when
            getWelcomeMessageWithContextError()

                    //then
                    .subscribe(Util.subscriber());
        }

        @Test
        void mono_withContext_absent_stepVerifier() {

            //when
            Mono<String> mono = getWelcomeMessageWithContextError();

            //then
            StepVerifier.create(mono)
                    .verifyErrorSatisfies(ex -> assertAll(
                            () -> assertEquals(RuntimeException.class, ex.getClass()),
                            () -> assertEquals("Unauthenticated", ex.getMessage())
                    ));
        }

        private Mono<String> getWelcomeMessageWithContextError() {
            return Mono
                    .deferContextual(contextView -> {
                        if (contextView.hasKey("user"))
                            return Mono.just("Welcome " + contextView.get("user"));
                        else
                            return Mono.error(new RuntimeException("Unauthenticated"));
                    });
        }
    }

    @Nested
    class ErrorMy {

        @Test
        void mono_withContext_present() {

            //when
            getWelcomeMessageWithContextError()

                    //then
                    .contextWrite(Context.of("user", "Art"))
                    .subscribe(Util.subscriber());
        }

        @Test
        void mono_withContext_absent() {

            //when
            getWelcomeMessageWithContextError()

                    //then
                    .subscribe(Util.subscriber());
        }

        @Test
        void mono_withContext_absent_stepVerifier() {

            //when
            Mono<String> mono = getWelcomeMessageWithContextError();

            //then
            StepVerifier.create(mono)
                    .verifyErrorSatisfies(ex -> assertAll(
                            () -> assertEquals(RuntimeException.class, ex.getClass()),
                            () -> assertEquals("Unauthenticated", ex.getMessage())
                    ));
        }

        private Mono<String> getWelcomeMessageWithContextError() {
            return Mono
                    .deferContextual(contextView -> Mono.just(
                            contextView
                                    .getOrEmpty("user")
                                    .map(user -> "Welcome " + user)
                                    .orElseThrow(() -> new RuntimeException("Unauthenticated"))));
        }
    }
}
