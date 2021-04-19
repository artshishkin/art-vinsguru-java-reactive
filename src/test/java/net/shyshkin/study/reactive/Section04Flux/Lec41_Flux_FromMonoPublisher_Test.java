package net.shyshkin.study.reactive.Section04Flux;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec41_Flux_FromMonoPublisher_Test {


    @Test
    void fromMono() {
        //given
        Mono<String> mono = Mono.just("a");

        //when
        Flux<String> flux = Flux.from(mono);

        //then
        flux.subscribe(Util.onNext, Util.onError, Util.onComplete);

    }
}
