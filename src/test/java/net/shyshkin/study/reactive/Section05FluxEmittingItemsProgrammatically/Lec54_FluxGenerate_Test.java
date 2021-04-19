package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec54_FluxGenerate_Test {

    @Test
    void generate_againAndAgain() {
        //given
        Flux.generate(
                synchronousSink -> {
                    String name = Util.FAKER.country().name();
                    synchronousSink.next(name);
                    log.debug("Emitting [{}] {}", Thread.currentThread().getName(), name);
                })
                //when - then
                .take(3)
                .subscribe(Util.subscriber());
    }

    @Test
    void generate_complete() {
        //given
        Flux.generate(
                synchronousSink -> {
                    String name = Util.FAKER.country().name();
                    synchronousSink.next(name);
                    log.debug("Emitting [{}] {}", Thread.currentThread().getName(), name);
                    synchronousSink.complete();
                })
                //when - then
                .take(3)
                .subscribe(Util.subscriber());
    }

    @Test
    void generate_error() {
        //given
        Flux.generate(
                synchronousSink -> {
                    String name = Util.FAKER.country().name();
                    synchronousSink.next(name);
                    log.debug("Emitting [{}] {}", Thread.currentThread().getName(), name);
                    synchronousSink.error(new RuntimeException("Fake Test Error"));
                })
                //when - then
                .take(3)
                .subscribe(Util.subscriber());
    }

    @Test
    void generate_untilCanada() {
        //given
        Flux.generate(
                synchronousSink -> {
                    String name = Util.FAKER.country().name();
                    synchronousSink.next(name);
                    log.debug("Emitting [{}] {}", Thread.currentThread().getName(), name);
                    if ("Canada".equals(name))
                        synchronousSink.complete();
                })
                //when - then
                .subscribe(Util.subscriber());
    }
}
