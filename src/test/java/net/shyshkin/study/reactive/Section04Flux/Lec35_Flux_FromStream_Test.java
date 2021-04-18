package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class Lec35_Flux_FromStream_Test {

    @Test
    @DisplayName("Stream can not be reused once it is closed")
    void stream_closed() {
        //given
        Stream<Integer> stream = Stream.of(1, 2, 3, 4);

        //when
        stream.forEach(System.out::println);

        Executable exec = () -> {
            stream.forEach(System.out::println);
        };

        //then
        assertThrows(IllegalStateException.class, exec);

    }

    @Test
    void fluxFromStream() {
        //given
        Stream<Integer> stream = Stream.of(1, 2, 3, 4);

        //when
        Flux<Integer> flux = Flux.fromStream(stream);

        //then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
        flux.subscribe(Util.onNext,
                ex -> {
                    log.debug("{}:{}", ex.getClass().getSimpleName(), ex.getMessage());
                    assertAll(
                            () -> assertEquals(IllegalStateException.class, ex.getClass()),
                            () -> assertEquals("stream has already been operated upon or closed", ex.getMessage())
                    );
                }
        );

    }

    @Test
    void fluxFromStreamSupplier() {
        //given
        Flux<Integer> flux = Flux.fromStream(() -> Stream.of(1, 2, 3, 4));

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
        flux.subscribe(Util.onNext, Assertions::assertNull, Util.onComplete);

    }

    @Test
    void fluxFromListStreamSupplier() {
        //given
        List<Integer> list = List.of(1, 2, 3, 4);
        Flux<Integer> flux = Flux.fromStream(list::stream);

        //when - then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);
        flux.subscribe(Util.onNext, Assertions::assertNull, Util.onComplete);

    }
}
