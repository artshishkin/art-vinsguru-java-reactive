package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec37_Flux_Log_Test {

    @Test
    void log() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);

        //when - then
        flux
                .log()
                .map(i -> "Name" + i)
                .subscribe(Util.onNext);
    }

    @Test
    void log_2() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);

        //when - then
        flux
                .log()
                .map(i -> "Name" + i)
                .log()
                .subscribe(Util.onNext);
    }
}
