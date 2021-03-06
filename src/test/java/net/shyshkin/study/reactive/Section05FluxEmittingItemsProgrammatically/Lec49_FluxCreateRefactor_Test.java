package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec49_FluxCreateRefactor_Test {

    @Test
    void manualProduce() throws InterruptedException {
        //given
        CountryNameProducer producer = new CountryNameProducer();

        //when - then
        Flux.create(producer)
                .subscribe(Util.subscriber());

        producer.produce();
        producer.produce();
        producer.produce();
        producer.complete();

        Thread.sleep(100);
    }

    @Test
    void differentThreads() {
        //given
        CountryNameProducer producer = new CountryNameProducer();

        //when - then
        Flux.create(producer)
                .subscribe(Util.subscriber());

        Runnable runnable = producer::produce;
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
        Util.sleep(0.2);
        producer.complete();

        Util.sleep(0.1);
    }
}
