package net.shyshkin.study.reactive.courseutil;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
@NoArgsConstructor
public class DefaultSubscriber implements Subscriber<Object> {

    private String name = "";

    public DefaultSubscriber(String name) {
        this.name = name + " - ";
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
    }

    @Override
    public void onComplete() {
        log.debug("{}Completed", name);
    }
}
