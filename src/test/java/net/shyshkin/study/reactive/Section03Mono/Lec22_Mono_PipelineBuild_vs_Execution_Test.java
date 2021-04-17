package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class Lec22_Mono_PipelineBuild_vs_Execution_Test {

    @Test
    void withoutSubscriber() {

        //when
        getName();
        getName();
        getName();

        //then
        System.out.println("No invocation");
    }

    @Test
    void withSubscriber() {

        //when
        Mono<String> mono = getName();

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    private Mono<String> getName() {

        System.out.println("Entered getName method");

        //building Pipeline
        return Mono.fromSupplier(
                () -> {
                    System.out.println("Generating name...");
                    Util.sleep(1);
                    return Util.FAKER.name().fullName();
                })
                .map(String::toUpperCase);
    }
}
