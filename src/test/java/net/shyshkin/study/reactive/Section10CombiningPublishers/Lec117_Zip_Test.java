package net.shyshkin.study.reactive.Section10CombiningPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec117_Zip_Test {

    @Test
    void zip_2() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);
        Flux<String> flux1 = getBody();
        Flux<String> flux2 = getEngine();

        //when
        Flux<Tuple2<String, String>> flux = flux1.zipWith(flux2);

        //then
        flux
                .doOnNext(tuple2 -> log.debug("{}:{}", tuple2.getT1(), tuple2.getT2()))
                .subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void zip_factory() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux.zip(getBody(), getEngine(), getTires(), getWindow(), getAirBag(), getBreak(), getGps(), getCd())

                //then
                .doOnNext(tuple -> log.debug("{}:{}:{}:{}:{}:{}:{}:{}",
                        tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4(),
                        tuple.getT5(), tuple.getT6(), tuple.getT7(), tuple.getT8()
                ))
                .subscribe(Util.subscriber(latch));
        latch.await();
    }

    @Test
    void zip_tuple_map() throws InterruptedException {

        //given
        CountDownLatch latch = new CountDownLatch(1);

        //when
        Flux.zip(getBody(), getEngine(), getTires())

                //then
                .map(tuple -> tuple
                        .mapT1(body -> body.toUpperCase() + Util.FAKER.random().nextInt(10, 99))
                        .mapT2(engine -> engine.toUpperCase() + Util.FAKER.random().nextInt(10, 99))
                        .mapT3(tires -> tires.toUpperCase() + Util.FAKER.random().nextInt(10, 99))
                )
                .doOnNext(tuple -> log.debug("{}:{}:{}",
                        tuple.getT1(), tuple.getT2(), tuple.getT3()
                ))
                .doOnNext(tuple -> tuple.forEach(o -> log.debug("{}:{}", o, o.getClass())))
                .map(tuple -> tuple.getT1() + "-" + tuple.getT2() + "-" + tuple.getT3())
                .subscribe(Util.subscriber(latch));
        latch.await();
    }

    private Flux<String> getBody() {
        return Flux.range(1, 2)
                .map(i -> "body");
    }

    private Flux<String> getTires() {
        return Flux.range(1, 6)
                .map(i -> "tires");
    }

    private Flux<String> getEngine() {
        return Flux.range(1, 3)
                .map(i -> "engine");
    }

    private Flux<String> getWindow() {
        return Flux.range(1, 3)
                .map(i -> "window");
    }

    private Flux<String> getAirBag() {
        return Flux.range(1, 3)
                .map(i -> "airbag");
    }

    private Flux<String> getBreak() {
        return Flux.range(1, 3)
                .map(i -> "break");
    }

    private Flux<String> getGps() {
        return Flux.range(1, 3)
                .map(i -> "gps");
    }

    private Flux<String> getCd() {
        return Flux.range(1, 3)
                .map(i -> "cd");
    }


}
