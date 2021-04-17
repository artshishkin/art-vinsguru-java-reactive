package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Lec25_Mono_FromFuture_Test {

    @Test
    void fromFuture() {
        //given
        Mono<String> mono = Mono.fromFuture(this::getName);

        //when
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);

        //then
        System.out.println("Completable Future executes using ForkJoinPool - asynchronously");
        Util.sleep(0.1);
    }

    private CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> Util.FAKER.name().fullName());
    }
}
