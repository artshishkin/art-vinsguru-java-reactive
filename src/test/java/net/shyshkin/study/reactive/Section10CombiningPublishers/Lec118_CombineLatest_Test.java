package net.shyshkin.study.reactive.Section10CombiningPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec118_CombineLatest_Test {

    @Nested
    class SimpleExample {


        @Test
        void combineLatest() throws InterruptedException {
            //given
            CountDownLatch latch = new CountDownLatch(1);

            //when
            Flux
                    .combineLatest(getString(), getIntegers(), (a, b) -> a + b)

                    //then
                    .subscribe(Util.subscriber(latch));
            latch.await();
        }

        private Flux<String> getString() {
            return Flux.just("A", "B", "C", "D")
                    .delayElements(Duration.ofMillis(100));
        }

        private Flux<Integer> getIntegers() {
            return Flux.range(1, 15)
                    .delayElements(Duration.ofMillis(33));
        }
    }

    @Nested
    class NameExample {

        @Test
        void combineLatest() throws InterruptedException {

            //given
            CountDownLatch latch = new CountDownLatch(1);
            Flux<String> flux1 = getFirstName();
            Flux<String> flux2 = getLastName();

            //when
            Flux<String> flux = Flux.combineLatest(flux1, flux2, (a, b) -> a + " " + b);

            //then
            flux
                    .take(10)
                    .subscribe(Util.subscriber(latch));
            latch.await();
        }


        private Flux<String> getFirstName() {
            return Flux
                    .generate(sink -> {
                        String firstName = Util.FAKER.name().firstName();
                        log.debug("firstname: {}", firstName);
                        sink.next(firstName);
                        Util.sleep(0.001 * Util.FAKER.random().nextInt(10, 100));
                    })
                    .cast(String.class)
                    .subscribeOn(Schedulers.boundedElastic());

        }

        private Flux<String> getLastName() {
            return Flux
                    .generate(sink -> {
                        String lastName = Util.FAKER.name().lastName();
                        log.debug("lastname: {}", lastName);
                        sink.next(lastName);
                        Util.sleep(0.001 * Util.FAKER.random().nextInt(10, 100));
                    })
                    .cast(String.class)
                    .subscribeOn(Schedulers.boundedElastic());
        }
    }

}
