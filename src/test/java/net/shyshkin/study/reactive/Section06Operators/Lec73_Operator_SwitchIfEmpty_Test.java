package net.shyshkin.study.reactive.Section06Operators;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec73_Operator_SwitchIfEmpty_Test {

    private Flux<Integer> sourceFlux;

    @BeforeEach
    void setUp() {
        sourceFlux = Flux.range(1, 10);
    }

    @Test
    void justCompleted() {
        //given
        sourceFlux

                //when
                .filter(i -> i > 10)

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void switchIfEmpty_flux() {
        //given
        sourceFlux

                //when
                .filter(i -> i > 10)
                .switchIfEmpty(onEmptyFlux())

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void switchIfEmpty_mono() {
        //given
        sourceFlux

                //when
                .filter(i -> i > 10)
                .switchIfEmpty(onEmptyMono())

                //then
                .subscribe(Util.subscriber());
    }

    private Flux<Integer> onEmptyFlux() {
        return Flux.range(100, 3);
    }

    private Mono<Integer> onEmptyMono() {
        return Mono.fromSupplier(() -> -3);
    }
}
