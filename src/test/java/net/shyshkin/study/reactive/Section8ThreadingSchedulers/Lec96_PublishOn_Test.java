package net.shyshkin.study.reactive.Section8ThreadingSchedulers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec96_PublishOn_Test {

    @Test
    void publishOn() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    for (int i = 0; i < 4 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("next (after):" + i))
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void publishOn_many() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    for (int i = 0; i < 4 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("next (after):" + i))
                .publishOn(Schedulers.parallel())
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    private void printThreadName(String msg) {
        String threadName = Thread.currentThread().getName();
        log.debug("[{}] {}", threadName, msg);
//        System.out.println(msg + "\t\t: Thread : " + threadName);
    }

}
