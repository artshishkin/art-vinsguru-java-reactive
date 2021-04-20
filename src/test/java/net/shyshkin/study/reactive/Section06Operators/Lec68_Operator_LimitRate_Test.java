package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec68_Operator_LimitRate_Test {

    @Test
    void rateLimiter_75percent() {
        //given
        Flux.range(1, 1000)
                .log()

                //when
                .limitRate(100)

                //then
                .subscribe(Util.subscriber());

        log.debug("request 100, after 75 request another 75 and so on");
    }

    @Test
    void rateLimiter_adjust() {
        //given
        Flux.range(1, 1000)
                .log()

                //when
                .limitRate(110,90)

                //then
                .subscribe(Util.subscriber());

        log.debug("request 110, after 90 request another 90 and so on");
    }
}
