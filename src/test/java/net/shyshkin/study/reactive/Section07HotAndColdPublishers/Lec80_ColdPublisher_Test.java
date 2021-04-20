package net.shyshkin.study.reactive.Section07HotAndColdPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Lec80_ColdPublisher_Test {

    @Test
    void coldPublisher() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(2);
        Flux<String> movieStream = Flux.fromStream(this::getMovie)
                .delayElements(Duration.ofMillis(100));

        //when
        movieStream
                .subscribe(Util.subscriber("Sub1", latch));
        Util.sleep(0.2);
        movieStream
                .subscribe(Util.subscriber("Sub2", latch));

        //then
        latch.await();
    }

    //netflix
    private Stream<String> getMovie() {
        log.debug("Got the movie streaming request");
        return IntStream.rangeClosed(1, 7)
                .mapToObj(i -> String.format("Scene %2d", i));
    }

}
