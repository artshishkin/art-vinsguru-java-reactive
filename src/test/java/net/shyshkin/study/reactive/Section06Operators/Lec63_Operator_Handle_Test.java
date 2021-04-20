package net.shyshkin.study.reactive.Section06Operators;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec63_Operator_Handle_Test {

    @Test
    void handle_doNothing() {
        //given
        // handle = filter + map
        Flux.range(1, 20)

                //when
                .handle(((integer, synchronousSink) -> {
                    synchronousSink.next(integer);
                }))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void handle_filter() {
        //given
        // handle = filter + map
        Flux.range(1, 20)

                //when
                .handle(((integer, synchronousSink) -> {
                    if (integer % 2 == 0)
                        synchronousSink.next(integer);
                }))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void handle_filterMap() {
        //given
        // handle = filter + map
        Flux.range(1, 20)

                //when
                .handle(((integer, synchronousSink) -> {
                    if (integer % 2 == 0)
                        synchronousSink.next(integer);
                    else
                        synchronousSink.next(integer + "a");
                }))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void handle_complete() {
        //given
        // handle = filter + map
        Flux.range(1, 20)

                //when
                .handle(((integer, synchronousSink) -> {
                    synchronousSink.next(integer + "a");
                    if (integer == 7) {
                        synchronousSink.complete();
                    }
                }))

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void handle_Ukraine() {
        //given
        Flux.range(1, Integer.MAX_VALUE)

                //when
                .handle((integer, synchronousSink) -> {
                    String countryName = Util.FAKER.country().name();
                    synchronousSink.next(countryName);
                    if ("Ukraine".equals(countryName)) synchronousSink.complete();
                })

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void handle_Ukraine_Vinsguru() {
        //given
        Flux.generate(synchronousSink -> synchronousSink.next(Util.FAKER.country().name()))

                //when
                .map(Object::toString)
                .handle((country, synchronousSink) -> {
                    synchronousSink.next(country);
                    if ("Ukraine".equals(country)) synchronousSink.complete();
                })

                //then
                .subscribe(Util.subscriber());
    }

    @Test
    void without_handle_Ukraine_Vinsguru_Art() {
        //given
        Flux.generate(synchronousSink -> synchronousSink.next(Util.FAKER.country().name()))

                //when
                .map(Object::toString)
                .takeUntil("Ukraine"::equals)

                //then
                .subscribe(Util.subscriber());
    }


}
