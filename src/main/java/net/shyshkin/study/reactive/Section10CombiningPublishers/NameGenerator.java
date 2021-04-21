package net.shyshkin.study.reactive.Section10CombiningPublishers;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Flux;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class NameGenerator {

    private final List<String> cache = new LinkedList<>();

    public Flux<String> generateNames() {
        return Flux
                .generate(
                        sink -> {
                            log.debug("generated fresh name");
                            String name = Util.FAKER.name().name();
                            Util.sleep(0.1);
                            cache.add(name);
                            sink.next(name);
                        })
                .cast(String.class)
                .startWith(getFromCache());
    }

    private Flux<String> getFromCache() {
        return Flux.fromIterable(cache);
    }

}
