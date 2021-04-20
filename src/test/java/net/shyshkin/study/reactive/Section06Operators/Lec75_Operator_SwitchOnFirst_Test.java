package net.shyshkin.study.reactive.Section06Operators;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.RepeatedTest;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
public class Lec75_Operator_SwitchOnFirst_Test {

    @RepeatedTest(5)
    void switchOnFirst() {
        //given
        getPersons()
                //when
                .switchOnFirst((sig, flux) ->
                        sig.isOnNext() && sig.get().getAge() > 15 ? flux : applyFilterMap().apply(flux)
                )
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
