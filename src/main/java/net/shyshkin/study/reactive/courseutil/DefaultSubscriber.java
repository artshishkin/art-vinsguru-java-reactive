package net.shyshkin.study.reactive.courseutil;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CountDownLatch;

@Slf4j
@NoArgsConstructor
public class DefaultSubscriber implements Subscriber<Object> {

    private String name = "";
    private CountDownLatch latch;

    public DefaultSubscriber(String name) {
        this(name, null);
    }

    public DefaultSubscriber(String name, CountDownLatch latch) {
        if (name != null)
            this.name = name + " - ";
        this.latch = latch;
    }

    public DefaultSubscriber(CountDownLatch latch) {
        this(null, latch);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {
        log.debug("{}Received: {}", name, o);
    }

    @Override
    public void onError(Throwable ex) {
        log.error("{}ERROR: {}:{}", name, ex.getClass().getSimpleName(), ex.getMessage());
        if (latch != null) latch.countDown();
    }

    @Override
    public void onComplete() {
        log.debug("{}Completed", name);
        if (latch != null) latch.countDown();
    }
}
