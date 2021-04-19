package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically;

import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class CountryNameProducer implements Consumer<FluxSink<String>> {

    private FluxSink<String> fluxSink;

    @Override
    public void accept(FluxSink<String> fluxSink) {
        this.fluxSink = fluxSink;
    }

    public void produce() {
        String countryName = Util.FAKER.country().name();
        String threadName = Thread.currentThread().getName();
        fluxSink.next(threadName + " : " + countryName);
    }

    public void complete() {
        fluxSink.complete();
    }
}
