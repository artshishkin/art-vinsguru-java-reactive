package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec48_FluxCreateDemo_Test {

    @Test
    void create_simple() {
        //given
        Flux
                .create(fluxSink -> {
                    fluxSink.next(1);
                    fluxSink.next(2);
                    fluxSink.complete();
                })

                //when - then
                .subscribe(Util.subscriber());
    }

    @Test
    void create_publishUntilCanada() {
        //given
        Flux
                .create(
                        fluxSink -> {
                            String countryName;
                            do {
                                countryName = Util.FAKER.country().name();
                                fluxSink.next(countryName);
                            } while (!"Canada".equals(countryName));
                            fluxSink.complete();
                        })

                //when - then
                .subscribe(Util.subscriber());
    }
}
