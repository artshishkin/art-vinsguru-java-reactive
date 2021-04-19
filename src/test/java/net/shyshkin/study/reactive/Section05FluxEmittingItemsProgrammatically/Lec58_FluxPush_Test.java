package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec58_FluxPush_Test {

    @Test
    void push() {
        //given
        CountryNameProducer producer = new CountryNameProducer();

        //when - then
        Flux.push(producer)
                .subscribe(Util.subscriber());

        Runnable runnable = producer::produce;
        for (int i = 0; i < 20; i++) {
            new Thread(runnable).start();
        }
        Util.sleep(0.2);
        producer.complete();

        log.debug("Something may be missing - it is not threadSafe");
        Util.sleep(0.1);
    }

}
