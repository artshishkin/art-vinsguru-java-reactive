package net.shyshkin.study.reactive.Section04Flux.assignment;

import net.shyshkin.study.reactive.courseutil.Util;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CountDownLatch;

class StockPriceObserver implements Subscriber<Integer> {

    private Subscription subscription;
    private final CountDownLatch latch;

    public StockPriceObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        this.subscription = s;
    }

    @Override
    public void onNext(Integer price) {
        Util.onNext.accept(price);
        if (price > 110 || price < 90) {
            subscription.cancel();
            latch.countDown();
        }
    }

    @Override
    public void onError(Throwable t) {
        Util.onError.accept(t);
        latch.countDown();
    }

    @Override
    public void onComplete() {
        Util.onComplete.run();
        latch.countDown();
    }
}
