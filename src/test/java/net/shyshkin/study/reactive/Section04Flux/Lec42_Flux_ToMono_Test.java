package net.shyshkin.study.reactive.Section04Flux;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class Lec42_Flux_ToMono_Test {

    @Test
    void next() {
        //given
        Flux<Integer> flux = Flux.range(1, 10);

        //when
        Mono<Integer> mono = flux.next();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void last() {
        //given
        Flux<Integer> flux = Flux.range(1, 10);

        //when
        Mono<Integer> mono = flux.last();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void elementAt() {
        //given
        Flux<Integer> flux = Flux.range(1, 10);

        //when
        Mono<Integer> mono = flux.elementAt(3);

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void ignoreElements_justCompleted() {
        //given
        Flux<Integer> flux = Flux.range(1, 10);

        //when
        Mono<Integer> mono = flux.ignoreElements();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void collectList() {
        //given
        Flux<Integer> flux = Flux.range(1, 10);

        //when
        Mono<List<Integer>> mono = flux.collectList();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void collectSortedList() {
        //given
        Flux<Integer> flux = Flux.just(1, 100, 10, 4, 3, 66, 7);

        //when
        Mono<List<Integer>> mono = flux.collectSortedList();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void reduce() {
        //given
        Flux<Integer> flux = Flux.just(1, 100, 10, 4, 3, 66, 7);

        //when
        Mono<Integer> mono = flux.reduce(Integer::sum);

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void findFirstElementGreaterThen3() {
        //given
        Flux<Integer> flux = Flux.just(1, 100, 10, 4, 3, 66, 7);

        //when
        Mono<Integer> mono = flux
                .filter(i -> i > 3)
                .next();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }
}
