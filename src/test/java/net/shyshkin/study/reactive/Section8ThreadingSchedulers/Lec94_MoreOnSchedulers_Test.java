package net.shyshkin.study.reactive.Section8ThreadingSchedulers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec94_MoreOnSchedulers_Test {

    @Test
    @DisplayName("Whole pipeline for the subscriber executes in a single thread of thread pool (both boundedElastic and parallel)")
    void subscribeOn_oneSubscriber() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    for (int i = 0; i < 4 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        Util.sleep(1);
                    }
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    @DisplayName("Pipelines for different subscribers execute in different threads")
    void subscribeOn_manySubscribers() throws InterruptedException {
        //given
        int subscribersCount = 4;
        CountDownLatch latch = new CountDownLatch(subscribersCount);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    for (int i = 0; i < 3 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        Util.sleep(1);
                    }
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        Runnable runnable = () -> flux
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        for (int i = 0; i < subscribersCount; i++) {
            new Thread(runnable).start();
        }

        latch.await();
    }

    @Test
    void subscribeOn_differentSubscribers() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(2);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    for (int i = 0; i < 4 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        Util.sleep(1);
                    }
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> printThreadName("boundedElastic subscriber receives: " + v), null, latch::countDown);
        flux
                .subscribeOn(Schedulers.parallel())
                .subscribe(v -> printThreadName("parallel subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }


    private void printThreadName(String msg) {
        String threadName = Thread.currentThread().getName();
        log.debug("[{}] {}", threadName, msg);
//        System.out.println(msg + "\t\t: Thread : " + threadName);
    }

}
