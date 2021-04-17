package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Lec15_MonoSubscribeTest {

    @Test
    void subscribe_withoutConsumer() {
        //given
        Mono<String> mono = Mono.just("ball");

        //when
        mono.subscribe();

        //then - no output
    }

    @Test
    void subscribe_allConsumers() {
        //given
        Mono<String> mono = Mono.just("ball");

        //when
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);

        //then - item and complete output
    }

    @Nested
    class Error {

        @Test
        @DisplayName("There is no exception in main thread just logging of exception")
        void subscribe_errorException() {
            //given
            Mono<Integer> mono = Mono.just("ball")
                    .map(String::length)
                    .map(l -> l / 0);

            //when
            mono.subscribe(
                    Util.onNext
            );

            //then - exception happens
        }

        @Test
        @Disabled("Exception does not go outside the Mono")
        void subscribe_errorExceptionAssert() throws InterruptedException {
            //given
            Mono<Integer> mono = Mono.just("ball")
                    .map(String::length)
                    .map(l -> l / 0);

            //when
            Executable exec = () -> {
                mono.subscribe(
                        Util.onNext
                );
            };

            //then
            Thread.sleep(100);
            assertThrows(ArithmeticException.class, exec);
        }

        @Test
        void subscribe_errorHandling() {
            //given
            Mono<Integer> mono = Mono.just("ball")
                    .map(String::length)
                    .map(l -> l / 0);

            //when
            mono.subscribe(
                    Util.onNext,
                    Util.onError,
                    Util.onComplete
            );

            //then - item and complete output
        }
    }
}
