package net.shyshkin.study.reactive.Section8ThreadingSchedulers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class Lec98_ParallelExecution_Test {

    @Test
    void parallel() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        ParallelFlux<Integer> flux = Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.parallel());

        //then
        flux
                .doOnNext(i -> printThreadName("next:" + i))
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void parallel_boundedElastic() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        ParallelFlux<Integer> flux = Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.boundedElastic());

        //then
        flux
                .doOnNext(i -> printThreadName("next:" + i))
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void parallel_setParallelism2() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        ParallelFlux<Integer> flux = Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.boundedElastic());

        //then
        flux
                .doOnNext(i -> printThreadName("next:" + i))
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void parallel_setParallelism10() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        ParallelFlux<Integer> flux = Flux.range(1, 10)
                .parallel(10)
                .runOn(Schedulers.boundedElastic());

        //then
        flux
                .doOnNext(i -> printThreadName("next:" + i))
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void parallel_convertToFlux() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        ParallelFlux<Integer> flux = Flux.range(1, 10)
                .parallel(10)
                .runOn(Schedulers.boundedElastic());

        //then
        flux
                .doOnNext(i -> printThreadName("next:" + i))
                .sequential()
                .publishOn(Schedulers.parallel())
                .subscribe(v -> printThreadName("subscriber receives: " + v), null, latch::countDown);

        latch.await();
    }

    @Test
    void parallel_arrayList() throws InterruptedException {
        //given
        int count = 1000;
        List<Integer> list = new ArrayList<>(count);
        CountDownLatch latch = new CountDownLatch(1);

        //when
        ParallelFlux<Integer> flux = Flux.range(1, count)
                .parallel()
                .runOn(Schedulers.parallel());

        //then
        flux
//                .doOnNext(i -> printThreadName("next:" + i))
                .subscribe(list::add, null, latch::countDown);

        latch.await();
        assertNotEquals(count, list.size());
    }

    private void printThreadName(String msg) {
        String threadName = Thread.currentThread().getName();
        log.debug("[{}] {}", threadName, msg);
//        System.out.println(msg + "\t\t: Thread : " + threadName);
    }

}
