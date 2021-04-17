package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    void withSubscriber_blocking() {

        //when
        getName();
        getName().subscribe(Util.onNext, Util.onError, Util.onComplete);
        getName();

        //then
        System.out.println("Subscriber method executes in main thread so it is blocking");
    }

    @Test
    void withSubscriber_nonBlocking() {

        //when
        getName();
        getName()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(Util.onNext, Util.onError, Util.onComplete);
        getName();

        //then
        System.out.println("Subscriber method executes in different thread so it is not blocking");
        Util.sleep(1.2);
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
