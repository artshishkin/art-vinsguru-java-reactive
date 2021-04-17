package net.shyshkin.study.reactive.Section03Mono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec14_MonoJustTest {

    @Test
    void noSubscriber() {
        //given
        Mono<Integer> mono = Mono.just(1);

        //when
        System.out.println(mono);

        //then
        System.out.println("Mono was not executed");

    }

    @Test
    void withSubscriber() {
        //given
        Mono<Integer> mono = Mono.just(1);

        //when
        mono.subscribe(System.out::println);

        //then
        System.out.println("Mono was executed");

    }

    @Test
    void withSubscriber_verifier() {
        //given
        Mono<Integer> mono = Mono.just(1);

        //when
        StepVerifier.create(mono)

                //then
                .expectNext(1)
                .verifyComplete();
    }
}
