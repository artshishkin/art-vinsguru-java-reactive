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
}
