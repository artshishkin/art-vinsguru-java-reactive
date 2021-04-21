package net.shyshkin.study.reactive.Section8ThreadingSchedulers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec90_PublisherSubscriber_DefaultThread_Test {

    @Test
    void defaultThread_main() {

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        flux.subscribe(v -> printThreadName("subscribe: " + v));

    }


    @Test
    void defaultThread_runnable() {

        //when
        Flux<Object> flux = Flux.create(
                fluxSink -> {
                    printThreadName("create");
                    fluxSink.next(1);
                })
                .doOnNext(i -> printThreadName("next:" + i));

        //then
        Runnable runnable = () -> flux.subscribe(v -> printThreadName("subscribe: " + v));
        for (int i = 0; i < 2; i++) {
            new Thread(runnable).start();
        }
        Util.sleep(0.1);
    }

    private void printThreadName(String msg) {
        String threadName = Thread.currentThread().getName();
        log.debug("[{}] {}", threadName, msg);
//        System.out.println(msg + "\t\t: Thread : " + threadName);
    }

}
