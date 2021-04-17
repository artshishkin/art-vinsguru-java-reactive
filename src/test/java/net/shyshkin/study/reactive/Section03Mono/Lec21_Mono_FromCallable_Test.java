package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Lec21_Mono_FromCallable_Test {

    @Test
    void fromSupplier() {
        //given

        //when
        Supplier<String> supplier = () -> generateName();
        Mono<String> mono = Mono.fromSupplier(supplier);

        //then
        mono.subscribe(Util.onNext);
    }

    @Test
    void fromSupplier_exception() {
        //given

        //when
        Supplier<String> supplier = () -> generateException();
        Mono<String> mono = Mono.fromSupplier(supplier);

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    @Test
    void fromCallable() {
        //given
        Callable<String> callable = () -> generateName();

        //when
        Mono<String> mono = Mono.fromCallable(callable);

        //then
        mono.subscribe(Util.onNext);
    }

    @Test
    void fromCallable_exception() {
        //given
        Callable<String> callable = () -> generateException();

        //when
        Mono<String> mono = Mono.fromCallable(callable);

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    private String generateName() {
        System.out.println("Generating name...");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Util.FAKER.name().fullName();
    }

    private String generateException() {
        System.out.println("Generating exception...");
        throw new RuntimeException("Fake Exception");
    }
}
