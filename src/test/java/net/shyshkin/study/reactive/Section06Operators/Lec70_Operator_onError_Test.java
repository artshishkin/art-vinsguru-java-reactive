package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Slf4j
public class Lec70_Operator_onError_Test {

    private Flux<Object> fluxWithErrorInCreate;
    private Flux<Integer> fluxWithErrorInPipeline;

    @BeforeEach
    void setUp() {
        fluxWithErrorInCreate = Flux
                .create(fluxSink -> {
                    fluxSink.next(1);
                    fluxSink.next(2);
                    fluxSink.error(new RuntimeException("Test Error"));
                    fluxSink.next(4);
                    fluxSink.complete();
                })
                .log();
        fluxWithErrorInPipeline = Flux.range(1, 10)
                .map(i -> 10 / (5 - i))
                .log();
    }

    @Test
    void errorThrown() {
        //given
        fluxWithErrorInCreate
                //when

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void onErrorReturn() {
        //given
        fluxWithErrorInCreate

                //when
                .onErrorReturn(-1)

                //then
                .subscribe(Util.subscriber());

        log.debug("Just return value and complete. Values after error are dropped.");
    }

    @Test
    void onErrorResume_fluxFallback() {
        //given
        fluxWithErrorInCreate

                //when
                .onErrorResume(ex -> fallbackFlux())

                //then
                .subscribe(Util.subscriber());

        log.debug("Resume with fallback values and complete. Values after error are dropped.");
    }

    @Test
    void onErrorResume_monoFallback() {
        //given
        fluxWithErrorInCreate
                //when
                .onErrorResume(ex -> fallbackMono())

                //then
                .subscribe(Util.subscriber());
        log.debug("Resume with fallback value and complete. Values after error are dropped.");
    }

    @Test
    void onErrorResume_monoFallback_inline() {
        //given
        Flux
                .create(fluxSink -> {
                    fluxSink.next(1);
                    fluxSink.next(2);
                    fluxSink.error(new RuntimeException("Test Error"));
                    fluxSink.next(4);
                    fluxSink.complete();
                })
                .log()            //when
                .onErrorResume(ex -> fallbackMono())

                //then
                .subscribe(Util.subscriber());
        log.debug("Resume with fallback value and complete. Values after error are dropped.");
    }

    private Flux<Integer> fallbackFlux() {
        return Flux.fromStream(() -> Stream.of(3, 12, 13, 14));
    }

    private Mono<Integer> fallbackMono() {
        return Mono.fromSupplier(() -> Util.FAKER.random().nextInt(100, 200));
    }


    @Test
    void onErrorContinue_errorInCreate() {
        //given
        fluxWithErrorInCreate

                //when
                .onErrorContinue((ex, obj) -> log.debug("error: {}, message: {}, object: {}", ex.getClass().getName(), ex.getMessage(), obj))

                //then
                .subscribe(Util.subscriber());
        log.debug("Error in create process just complete pipeline and we can not continue");
    }

    @Test
    void onErrorContinue_errorInPipeline() {
        //given
        fluxWithErrorInPipeline

                //when
                .onErrorContinue((ex, obj) -> log.debug("error: {}, message: {}, object: {}", ex.getClass().getName(), ex.getMessage(), obj))

                //then
                .subscribe(Util.subscriber());

        log.debug("Error in pipeline (like map operator) - pipeline continues");
    }
}
