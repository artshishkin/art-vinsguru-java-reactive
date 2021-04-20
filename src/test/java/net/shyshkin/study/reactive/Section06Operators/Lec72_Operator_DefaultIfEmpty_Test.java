package net.shyshkin.study.reactive.Section06Operators;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec72_Operator_DefaultIfEmpty_Test {

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
    void defaultIfEmpty() {
        //given
        sourceFlux

                //when
                .filter(i -> i > 10)
                .defaultIfEmpty(666)

                //then
                .subscribe(Util.subscriber());
    }
}
