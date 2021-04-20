package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec65_Operator_DoHooksOrCallbacks_Test {

    @Test
    void callbacks_doFirstAndSubscribeOrder() {
        //given
        Flux.create(fluxSink -> {
            log.debug("inside create");
            for (int i = 0; (i < 5) && !fluxSink.isCancelled(); i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
            log.debug("--completed");
        })
                //when
                .doFirst(() -> log.debug("doFirst 1"))
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe 1: {}", subscription))
                .doFirst(() -> log.debug("doFirst 2"))
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe 2 - goes from publisher to subscriber: {}", subscription))
                .doFinally(signal -> log.debug("doFinally: {}", signal))
                .doFirst(() -> log.debug("doFirst 3 - goes from subscriber to publisher"))
                .doOnDiscard(Object.class, o -> log.debug("doOnDiscard: {}", o))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void callbacks() {
        //given
        Flux.create(fluxSink -> {
            log.debug("inside create");
            for (int i = 0; (i < 5) && !fluxSink.isCancelled(); i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
            log.debug("--completed");
        })
                //when
                .doOnComplete(() -> log.debug("doOnComplete"))
                .doFirst(() -> log.debug("doFirst"))
                .doOnNext(o -> log.debug("doOnNext: {}", o))
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe: {}", subscription))
                .doOnRequest(count -> log.debug("Requested {} elements", count))
                .doOnError(ex -> log.error("doOnError: {}:{}", ex.getClass(), ex.getMessage()))
                .doOnTerminate(() -> log.debug("doOnTerminate"))
                .doOnCancel(() -> log.debug("doOnCancel"))
                .doFinally(signal -> log.debug("doFinally: {}", signal))
                .doOnDiscard(Object.class, o -> log.debug("doOnDiscard: {}", o))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void callbacks_error() {
        //given
        Flux.create(fluxSink -> {
            log.debug("inside create");
            for (int i = 0; (i < 5) && !fluxSink.isCancelled(); i++) {
                fluxSink.next(i);
            }
            fluxSink.error(new RuntimeException("Fake Exception"));
            log.debug("--completed");
        })
                //when
                .doOnComplete(() -> log.debug("doOnComplete"))
                .doFirst(() -> log.debug("doFirst"))
                .doOnNext(o -> log.debug("doOnNext: {}", o))
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe: {}", subscription))
                .doOnRequest(count -> log.debug("Requested {} elements", count))
                .doOnError(ex -> log.error("doOnError: {}:{}", ex.getClass(), ex.getMessage()))
                .doOnTerminate(() -> log.debug("doOnTerminate"))
                .doOnCancel(() -> log.debug("doOnCancel"))
                .doFinally(signal -> log.debug("doFinally: {}", signal))
                .doOnDiscard(Object.class, o -> log.debug("doOnDiscard: {}", o))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void callbacks_cancel_discard() {
        //given
        Flux.create(fluxSink -> {
            log.debug("inside create");
            for (int i = 0; (i < 5) /*&& !fluxSink.isCancelled()*/; i++) {
                fluxSink.next(i);
            }
            fluxSink.error(new RuntimeException("Fake Exception"));
            log.debug("--completed");
        })
                //when
                .doOnComplete(() -> log.debug("doOnComplete"))
                .doFirst(() -> log.debug("doFirst"))
                .doOnNext(o -> log.debug("doOnNext: {}", o))
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe: {}", subscription))
                .doOnRequest(count -> log.debug("Requested {} elements", count))
                .doOnError(ex -> log.error("doOnError: {}:{}", ex.getClass(), ex.getMessage()))
                .doOnTerminate(() -> log.debug("doOnTerminate"))
                .doOnCancel(() -> log.debug("doOnCancel"))
                .doFinally(signal -> log.debug("doFinally: {}", signal))
                .doOnDiscard(Object.class, o -> log.debug("doOnDiscard: {}", o))
                .take(2)

                //then
                .subscribe(Util.subscriber());

        Util.sleep(1);
    }


    @Test
    void callbacks_cancel_finally() {
        //given
        Flux.create(fluxSink -> {
            log.debug("inside create");
            for (int i = 0; (i < 5) && !fluxSink.isCancelled(); i++) {
                fluxSink.next(i);
            }
            fluxSink.error(new RuntimeException("Fake Exception"));
            log.debug("--completed");
        })
                //when
                .doOnComplete(() -> log.debug("doOnComplete"))
                .doFirst(() -> log.debug("doFirst"))
                .doOnNext(o -> log.debug("doOnNext: {}", o))
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe: {}", subscription))
                .doOnRequest(count -> log.debug("Requested {} elements", count))
                .doOnError(ex -> log.error("doOnError: {}:{}", ex.getClass(), ex.getMessage()))
                .doOnTerminate(() -> log.debug("doOnTerminate"))
                .doOnCancel(() -> log.debug("doOnCancel"))
                .doFinally(signal -> log.debug("doFinally 1 - is not the last because of take(): {}", signal))
                .doOnDiscard(Object.class, o -> log.debug("doOnDiscard: {}", o))
                .take(2)
                .doFinally(signal -> log.debug("doFinally 2 - last: {}", signal))

                //then
                .subscribe(Util.subscriber());

        Util.sleep(1);
    }
}
