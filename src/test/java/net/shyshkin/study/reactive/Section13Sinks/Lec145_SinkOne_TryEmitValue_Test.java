package net.shyshkin.study.reactive.Section13Sinks;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

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
}
