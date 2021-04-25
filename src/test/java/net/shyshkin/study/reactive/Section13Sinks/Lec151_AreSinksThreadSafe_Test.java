package net.shyshkin.study.reactive.Section13Sinks;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class Lec151_AreSinksThreadSafe_Test {

    @Test
    void threadSafety_notSafe() {
        //given
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();
        List<Object> list = new ArrayList<>();
        Flux<Object> flux = sink.asFlux();

        //when
        flux
                .subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                sink.tryEmitNext(finalI);
            });
        }

        //then
        Util.sleep(2);
        log.debug("Size of list {}/1000", list.size());
    }

    @Test
    void threadSafety_safe() {
        //given
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();
        List<Object> list = new ArrayList<>();
        Flux<Object> flux = sink.asFlux();

        //when
        flux
//                .doOnNext(Util.onNext)
                .subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                sink.emitNext(finalI, (signalType, emitResult) -> {
                    log.debug("{}:{}", signalType, emitResult);
                    return true;
                });
            });
        }

        //then
        Util.sleep(2);
        log.debug("Size of list {}/1000", list.size());

        List<Object> distinctList = list.stream().distinct().collect(Collectors.toList());
        assertEquals(1000, distinctList.size());
    }
}
