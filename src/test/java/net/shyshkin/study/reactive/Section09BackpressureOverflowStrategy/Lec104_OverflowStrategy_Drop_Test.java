package net.shyshkin.study.reactive.Section09BackpressureOverflowStrategy;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec104_OverflowStrategy_Drop_Test {

    @Test
    void drop() throws InterruptedException {
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
                .onBackpressureDrop()
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> Util.sleep(0.01))

                //then
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void drop_changeBufferSize() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.create(
                fluxSink -> {
                    for (int i = 1; i < 501 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        log.debug("Pushed: {}", i);
                        Util.sleep(0.001);
                    }
                    fluxSink.complete();
                })

                //when
                .onBackpressureDrop()
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> Util.sleep(0.01))

                //then
                .subscribe(Util.subscriber(latch));

        latch.await();
    }

    @Test
    void drop_captureDropped() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        List<Object> dbDroppedItems = new ArrayList<>();

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.create(
                fluxSink -> {
                    for (int i = 1; i < 201 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        log.debug("Pushed: {}", i);
                        Util.sleep(0.001);
                    }
                    fluxSink.complete();
                })

                //when
                .onBackpressureDrop(dbDroppedItems::add)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> Util.sleep(0.01))

                //then
                .subscribe(Util.subscriber(latch));

        latch.await();

        log.debug("Total count of dropped items: {}", dbDroppedItems.size());
        System.out.println(dbDroppedItems);

    }
}
