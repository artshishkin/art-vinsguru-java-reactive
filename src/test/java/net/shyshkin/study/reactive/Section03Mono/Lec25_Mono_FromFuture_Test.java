package net.shyshkin.study.reactive.Section03Mono;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class Lec25_Mono_FromFuture_Test {

    @Test
    void fromFuture() {
        //given
        Mono<String> mono = Mono.fromFuture(this::getName);

        //when
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);

        //then
        log.debug("Completable Future executes using ForkJoinPool - asynchronously");
        Util.sleep(0.1);
    }

    private CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> Util.FAKER.name().fullName());
    }
}
