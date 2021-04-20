package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
public class Lec74_Operator_Transform_Test {

    @Test
    void transform() {
        //given
        getPersons()
                //when
                .transform(applyFilterMap())
                //then
                .subscribe(Util.subscriber());

    }

    private Function<Flux<Person>, Flux<Person>> applyFilterMap() {
        return flux -> flux
                .filter(person -> person.getAge() > 7)
                .doOnNext(person -> person.setName(person.getName().toUpperCase()))
                .doOnDiscard(Person.class, person -> log.debug("Discarded {}", person));
    }

    private Flux<Person> getPersons() {
        return Flux.range(1, 10)
                .map(i -> new Person());
    }


}
