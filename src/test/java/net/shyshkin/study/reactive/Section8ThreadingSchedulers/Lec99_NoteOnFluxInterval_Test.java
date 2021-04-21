package net.shyshkin.study.reactive.Section8ThreadingSchedulers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class Lec99_NoteOnFluxInterval_Test {

    @Test
    @DisplayName("interval() uses Schedulers.parallel()")
    void interval() {

        //when
        Flux.interval(Duration.ofMillis(100))

                //then
                .subscribe(Util.subscriber());
        Util.sleep(1);
    }

    @Test
    @DisplayName("trying to modify to boundedElastic does not work")
    void interval_subscribeOnDoesNotWork() {

        //when
        Flux.interval(Duration.ofMillis(100))

                //then
                .doOnNext(i -> log.debug("{}", i))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(Util.subscriber());
        Util.sleep(1);
    }

    @Test
    void interval_publishOn() {

        //when
        Flux.interval(Duration.ofMillis(100))

                //then
                .doOnNext(i -> log.debug("{}", i))
                .publishOn(Schedulers.boundedElastic())
                .subscribe(Util.subscriber());
        Util.sleep(1);
    }
}
