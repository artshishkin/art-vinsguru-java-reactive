package net.shyshkin.study.reactive.Section8ThreadingSchedulers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec92_SubscribeOn_Test {

    @Test
    void subscribeOn() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .doFirst(() -> printThreadName("first2"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1"))
                .subscribe(v -> printThreadName("subscribe: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void subscribeOn_threads() throws InterruptedException {
        //given
        int threadsCount = 2;
        CountDownLatch latch = new CountDownLatch(threadsCount);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        Runnable runnable = () -> flux
                .doFirst(() -> printThreadName("first2"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1"))
                .subscribe(v -> printThreadName("subscribe: " + v), null, latch::countDown);

        for (int i = 0; i < threadsCount; i++) {
            new Thread(runnable).start();
        }

        latch.await();
    }

    @Test
    void multiple_subscribeOn() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                .subscribeOn(Schedulers.newParallel("art"))
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .doFirst(() -> printThreadName("first2"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1"))
                .subscribe(v -> printThreadName("subscribe: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void multiple_subscribeOn_elastic() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux
                .doFirst(() -> printThreadName("first2"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1"))
                .subscribe(v -> printThreadName("subscribe: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void multiple_subscribeOn_threads() throws InterruptedException {
        //given
        int threadsCount = 2;
        CountDownLatch latch = new CountDownLatch(threadsCount);

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                .subscribeOn(Schedulers.newParallel("art"))
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        Runnable runnable = () -> flux
                .doFirst(() -> printThreadName("first2"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1"))
                .subscribe(v -> printThreadName("subscribe: " + v), null, latch::countDown);

        for (int i = 0; i < threadsCount; i++) {
            new Thread(runnable).start();
        }

        latch.await();
    }

    private void printThreadName(String msg) {
        String threadName = Thread.currentThread().getName();
        log.debug("[{}] {}", threadName, msg);
//        System.out.println(msg + "\t\t: Thread : " + threadName);
    }

}
