package net.shyshkin.study.reactive.Section03Mono;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
public class Lec26_Mono_FromRunnable_Test {


    @Test
    void notificationExample() {
        //given
        Mono<Object> mono = methodMono();

        //when
        log.debug("Notification only happens when subscriber subscribes");
        Util.sleep(0.1);
        mono
                .then(Mono.just("Some other operation"))
                .subscribe(Util.onNext, Util.onError, Util.onComplete);

        //then
        log.debug("Execution finished");

    }

    private Runnable notification() {
        return () -> log.debug("Some notification. Method was invoked {}", LocalDateTime.now());
    }

    private Mono<Object> methodMono() {
        return Mono.fromRunnable(notification());
    }

    @Test
    void noOnNextCall() {
        //given
        Runnable runnable = () -> log.debug("Some method execution");

        //when
        Mono.fromRunnable(runnable)
                .subscribe(Util.onNext, Util.onError, Util.onComplete);

        //then
        log.debug("When use from Runnable onNext is not invoked. Execution is in the MAIN thread");
    }

    @Test
    void timeConsumingProcessExample() {

        //when
        log.debug("Start execution");
        Mono.fromRunnable(timeConsumingProcess())
                .subscribe(
                        Util.onNext,
                        Util.onError,
                        onCompletionNotification()
                );

        //then
        log.debug("MAIN thread finished");
    }

    private Runnable timeConsumingProcess() {
        return () -> {
            Util.sleep(0.5);
            log.debug("Operation completed");
        };
    }

    private Runnable onCompletionNotification() {
        return () -> {
            log.debug("Process is finished. Sending email...");
        };
    }
}
