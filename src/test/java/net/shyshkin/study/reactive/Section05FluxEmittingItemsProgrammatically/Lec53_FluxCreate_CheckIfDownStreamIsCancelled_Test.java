package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec53_FluxCreate_CheckIfDownStreamIsCancelled_Test {

    @Test
    void create_publishUntilCanada_WRONG() {
        //given
        Flux
                .create(
                        fluxSink -> {
                            String countryName;
                            do {
                                countryName = Util.FAKER.country().name();
                                String threadName = Thread.currentThread().getName();
                                log.debug("[{}] Emitting : `{}`", threadName, countryName);
                                fluxSink.next(countryName);
                            } while (!"Canada".equals(countryName));
                            fluxSink.complete();
                        })

                //when - then
                .take(3)
                .subscribe(Util.subscriber());
    }

    @Test
    void create_publishUntilCanada_CORRECT() {
        //given
        Flux
                .create(
                        fluxSink -> {
                            String countryName;
                            do {
                                countryName = Util.FAKER.country().name();
                                String threadName = Thread.currentThread().getName();
                                log.debug("[{}] Emitting : `{}`", threadName, countryName);
                                fluxSink.next(countryName);
                            } while (!"Canada".equals(countryName) && !fluxSink.isCancelled());
                            fluxSink.complete();
                        })

                //when - then
                .take(3)
                .subscribe(Util.subscriber());
    }


}
