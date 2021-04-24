package net.shyshkin.study.reactive.Section13Sinks;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec145_SinkOne_TryEmitValue_Test {

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        latch.await();
    }

    @Test
    void sink_tryEmit() {
        //given
        //mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        log.debug("Start sink test");

        //when
        Mono<Object> mono = sink.asMono();
        mono
                .take(Duration.ofSeconds(3))
                .subscribe(Util.subscriber(latch));
        //then
        Util.sleep(1);
        sink.tryEmitValue("HI");
    }

    @Test
    void sink_tryEmitEmpty() {
        //given
        //mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        log.debug("Start sink test");

        //when
        Mono<Object> mono = sink.asMono();
        mono
                .subscribe(Util.subscriber(latch));
        //then
        Util.sleep(1);
        sink.tryEmitEmpty();
    }

    @Test
    void sink_tryEmitError() {
        //given
        //mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        log.debug("Start sink test");

        //when
        Mono<Object> mono = sink.asMono();
        mono
                .subscribe(Util.subscriber(latch));
        //then
        Util.sleep(1);
        sink.tryEmitError(new IllegalStateException("Some fake exception"));
    }

    @Test
    void sink_emitValue() {
        //given
        //mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        log.debug("Start sink test");

        //when
        Mono<Object> mono = sink.asMono();
        mono
                .subscribe(Util.subscriber(latch));
        //then
        Util.sleep(1);

        sink.emitValue(
                "hi",
                (signalType, emitResult) -> {
                    log.debug("{}:{}:{}", signalType.name(), signalType, emitResult.name());
                    return false; //retry?
                }
        );
    }

    @Test
    void sink_emitValue_errorNoRetry() {
        //given
        //mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        log.debug("Start sink test");

        //when
        Mono<Object> mono = sink.asMono();
        mono
                .map(i -> i)
                .subscribe(Util.subscriber(latch));
        //then
        Util.sleep(1);

        sink.emitValue(
                "hi",
                (signalType, emitResult) -> {
                    log.debug("{}:{}:{}", signalType.name(), signalType, emitResult.name());
                    return false; //retry?
                }
        );
        sink.emitValue(
                "hello",
                (signalType, emitResult) -> {
                    log.debug("{}:{}:{}", signalType.name(), signalType, emitResult.name());
                    return false; //retry?
                }
        );
    }

    @Test
    void sink_emitValue_error_withForeverRetry() {
        //given
        //mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        log.debug("Start sink test");

        //when
        Mono<Object> mono = sink.asMono();
        mono
                .publishOn(Schedulers.boundedElastic())
                .subscribe(Util.subscriber(latch));
        //then
        Util.sleep(1);

        Runnable runnable = () ->
                sink.emitValue(
                        "hi from " + Thread.currentThread().getName(),
                        (signalType, emitResult) -> {
                            log.debug("{}:{}:{}", signalType.name(), signalType, emitResult.name());
                            Util.sleep(0.1);
                            return true; //retry?
                        }
                );

        for (int i = 0; i < 2; i++) {
            new Thread(runnable).start();
            Util.sleep(0.01);
        }

        Util.sleep(2);
    }
}
