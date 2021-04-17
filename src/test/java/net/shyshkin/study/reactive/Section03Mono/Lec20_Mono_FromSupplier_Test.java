package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class Lec20_Mono_FromSupplier_Test {


    @Test
    @DisplayName("WRONG - Time consuming process invokes even if subscriber does NOT subscribe publisher")
    void wrongImplementation() {
        //given

        //when
        Mono<String> mono = Mono.just(generateName());

        //then
        System.out.println("Use method `just` only if you have data already");

    }

    @Test
    @DisplayName("CORRECT - Time consuming process only invokes when subscriber subscribes publisher")
    void correctImplementation() {
        //given

        //when
        Mono<String> mono = Mono.fromSupplier(() -> generateName());

        //then
        mono.subscribe(Util.onNext);
    }

    private String generateName() {
        System.out.println("Generating name...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Util.FAKER.name().fullName();
    }
}
