package net.shyshkin.study.reactive.courseutil;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class DefaultSubscriberTest {

    @Test
    void onNext_unnamed() {
        //given
        Mono<String> mono = Mono.just("Hello");

        //when - then
        mono.subscribe(new DefaultSubscriber());
    }

    @Test
    void onNext_unnamed_Util() {
        //given
        Mono<String> mono = Mono.just("Hello");

        //when - then
        mono.subscribe(Util.subscriber());
    }

    @Test
    void onNext_named() {
        //given
        Mono<String> mono = Mono.just("Hello");

        //when - then
        mono.subscribe(new DefaultSubscriber("Art"));
    }

    @Test
    void onError_unnamed() {
        //given
        Mono<String> mono = Mono.error(new RuntimeException("Hello"));

        //when - then
        mono.subscribe(new DefaultSubscriber());
    }

    @Test
    void onError_named() {
        //given
        Mono<String> mono = Mono.error(new RuntimeException("Hello"));

        //when - then
        mono.subscribe(new DefaultSubscriber("Art"));
    }

    @Test
    void onError_named_Util() {
        //given
        Mono<String> mono = Mono.error(new RuntimeException("Hello"));

        //when - then
        mono.subscribe(Util.subscriber("Art"));
    }
}