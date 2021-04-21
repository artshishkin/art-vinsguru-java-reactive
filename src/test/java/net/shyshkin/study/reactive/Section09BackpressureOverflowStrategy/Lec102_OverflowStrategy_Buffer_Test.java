package net.shyshkin.study.reactive.Section09BackpressureOverflowStrategy;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec102_OverflowStrategy_Buffer_Test {

    @Test
    @DisplayName("All the items are pushed before any was received")
    void defaultBehaviour() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        Flux.create(
                fluxSink -> {
                    for (int i = 1; i < 501 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        log.debug("Pushed: {}", i);
                    }
                    fluxSink.complete();
                })

                //when
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> Util.sleep(0.01))

                //then
                .subscribe(Util.subscriber(latch));

        latch.await();
    }
}
