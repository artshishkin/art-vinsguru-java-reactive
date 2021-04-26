package net.shyshkin.study.reactive.Section15UnitTestingWithStepVerifier;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec173_StepVerifier_VirtualTimeTest {

    @Test
    void withVirtualTime_thenAwait() {
        //then
        StepVerifier.withVirtualTime(this::timeConsumingFlux)
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    @Test
    void withVirtualTime_expectNoEvent() {
        //then
        StepVerifier.withVirtualTime(this::timeConsumingFlux)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(4))
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    private Flux<String> timeConsumingFlux() {
        return Flux.range(1, 4)
                .delayElements(Duration.ofSeconds(5))
                .map(i -> i + "a")
                .doOnNext(Util.onNext);
    }

}
