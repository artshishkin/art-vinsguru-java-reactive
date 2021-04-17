package net.shyshkin.study.reactive.courseutil;

import com.github.javafaker.Faker;

import java.util.function.Consumer;

public class Util {

    public static final Consumer<Object> onNext = o -> System.out.println("Received: " + o);
    public static final Consumer<Throwable> onError = ex -> System.out.printf("ERROR: %s:%s", ex.getClass().getSimpleName(), ex.getMessage());
    public static final Runnable onComplete = () -> System.out.println("Completed");

    public static final Faker FAKER = Faker.instance();

    public static void sleep(double seconds) {
        try {
            Thread.sleep((long) (1000 * seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
