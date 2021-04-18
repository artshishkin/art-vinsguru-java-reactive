package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class Lec34_Flux_FromArrayOrList_Test {

    @Test
    void array() {
        //given
        Integer[] array = {1, 2, 3, 4, 5, 6};
        Flux<Integer> flux = Flux.fromArray(array);

        //when - then
        flux.subscribe(Util.onNext);

    }

    @Test
    void list() {
        //given
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        Flux<Integer> flux = Flux.fromIterable(list);

        //when - then
        flux.subscribe(Util.onNext);
    }
}
