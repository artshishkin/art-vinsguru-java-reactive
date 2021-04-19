package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec52_TakeOperator_Test {

    @Test
    void take() {
        //given
        Flux<Integer> flux = Flux.range(1, 10);

        //when - then
        flux
                .log()
                .take(3) //cancels the upstream subscription when took 3 elements and sends onComplete to the downstream subscriber
                .log()
                .subscribe(Util.subscriber());
    }
}
