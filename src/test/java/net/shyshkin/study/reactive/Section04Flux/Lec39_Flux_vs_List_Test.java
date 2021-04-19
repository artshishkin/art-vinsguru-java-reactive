package net.shyshkin.study.reactive.Section04Flux;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class Lec39_Flux_vs_List_Test {

    @Test
    void list() {

        //when
        List<String> list = NameGenerator.generateNameList(5);

        //then
        list.forEach(s->log.debug("{}",s));
        log.debug("Wait 5s to receive any result {}", list);
    }

    @Test
    void flux() {

        //when
        Flux<String> flux = NameGenerator.generateNameFlux(5);

        //then
        flux.subscribe(Util.onNext);
        log.debug("Wait 1s to receive any result");
    }
}
