package net.shyshkin.study.reactive.Section04Flux;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec36_Flux_Range_Test {

    @Test
    void range() {
        //given
        Flux<Integer> flux = Flux.range(100, 6);

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);

    }

    @Test
    @DisplayName("ASSIGNMENT: print name 10 times")
    void assignment() {
        //given
        Flux<String> flux = Flux
                .range(1, 10)
                .map(i -> Util.FAKER.name().fullName());

        //when - then
        flux.subscribe(System.out::println);

    }
}
