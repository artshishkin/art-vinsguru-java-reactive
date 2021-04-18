package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec32_Flux_Just_Test {

    @Test
    void just_1() {
        //given
        Flux<Integer> flux = Flux.just(1);

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void just_many() {
        //given
        log.debug("Start");
        Flux<Integer> flux = Flux.just(1, 2, 3);

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void empty() {
        //given
        log.debug("Start");
        Flux<Integer> flux = Flux.empty();

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void objectFlux() {
        //given
        log.debug("Start");
        Flux<Object> flux = Flux.just(1, 2, 3L, "foo", "buzz", Util.FAKER.name().fullName());

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }
}
