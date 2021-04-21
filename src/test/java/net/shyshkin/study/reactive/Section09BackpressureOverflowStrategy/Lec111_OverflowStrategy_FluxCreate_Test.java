package net.shyshkin.study.reactive.Section09BackpressureOverflowStrategy;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec111_OverflowStrategy_FluxCreate_Test {

    @Test
    void create_drop() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.create(
                fluxSink -> {
                    for (int i = 1; i < 201 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        log.debug("Pushed: {}", i);
                        Util.sleep(0.001);
                    }
                    fluxSink.complete();
                }, FluxSink.OverflowStrategy.DROP)

                //when
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> Util.sleep(0.01))

                //then
                .subscribe(Util.subscriber(latch));

        latch.await();
    }
}
