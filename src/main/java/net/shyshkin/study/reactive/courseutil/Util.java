package net.shyshkin.study.reactive.courseutil;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;

import java.util.function.Consumer;

@Slf4j
public class Util {

    public static final Consumer<Object> onNext = o -> log.debug("Received: {}", o);
    public static final Consumer<Throwable> onError = ex -> log.error("ERROR: {}:{}", ex.getClass().getSimpleName(), ex.getMessage());
    public static final Runnable onComplete = () -> log.debug("Completed");

    public static final Faker FAKER = Faker.instance();

    public static void sleep(double seconds) {
        try {
            Thread.sleep((long) (1000 * seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Subscriber<Object> subscriber() {
        return new DefaultSubscriber();
    }

    public static Subscriber<Object> subscriber(String name) {
        return new DefaultSubscriber(name);
    }
}
