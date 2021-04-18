package net.shyshkin.study.reactive.Section04Flux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Slf4j
public class Lec38_Flux_CustomSubscriberImplementation_Test {

    @Test
    void customSubscriber_doNotRequest() {
        //given
        Flux<Integer> flux = Flux.range(1, 20);

        //when
        Consumer<Subscription> subscriptionConsumer = (subs) -> {
        };

        flux
                .log()
                .subscribe(new MyCustomSubscriber(subscriptionConsumer));

        //then
        log.debug("Only subscription");
    }

    @Test
    void customSubscriber_request5() {
        //given
        Flux<Integer> flux = Flux.range(1, 20);

        //when
        Consumer<Subscription> subscriptionConsumer = (subs) -> {
            subs.request(5);
        };

        flux
                .log()
                .subscribe(new MyCustomSubscriber(subscriptionConsumer));

        //then
        log.debug("Return 5");
    }

    @Test
    void customSubscriber_requestUnbounded() {
        //given
        Flux<Integer> flux = Flux.range(1, 20);

        //when
        Consumer<Subscription> subscriptionConsumer = (subs) -> {
            subs.request(Long.MAX_VALUE);
        };

        flux
                .log()
                .subscribe(new MyCustomSubscriber(subscriptionConsumer));

        //then
        log.debug("Return ALL");
    }

    @Test
    void customSubscriber_playingWithRequests_throughAtomicReference() {
        //given
        Flux<Integer> flux = Flux.range(1, 20);
        AtomicReference<Subscription> atomicReference = new AtomicReference<>();

        //when
        Consumer<Subscription> subscriptionConsumer = (subs) -> {
            atomicReference.set(subs);
        };

        flux
                .log()
                .subscribe(new MyCustomSubscriber(subscriptionConsumer));

        //then
        Util.sleep(0.1);
        atomicReference.get().request(3);
        Util.sleep(0.1);
        atomicReference.get().request(5);
        Util.sleep(0.1);
        log.debug("Going to Cancel");
        atomicReference.get().cancel();
        Util.sleep(0.2);
        log.debug("Waiting for more 2 items");
        atomicReference.get().request(2);
        Util.sleep(0.2);
        log.debug("But we can not receive them because subscription already CANCELLED");
    }


    @RequiredArgsConstructor
    private static class MyCustomSubscriber implements Subscriber<Integer> {

        private final Consumer<Subscription> subscriptionConsumer;

        @Override
        public void onSubscribe(Subscription subscription) {
            subscriptionConsumer.accept(subscription);
        }

        @Override
        public void onNext(Integer integer) {
            Util.onNext.accept(integer);
        }

        @Override
        public void onError(Throwable throwable) {
            Util.onError.accept(throwable);
        }

        @Override
        public void onComplete() {
            Util.onComplete.run();
        }
    }
}
