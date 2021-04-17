package net.shyshkin.study.reactive.Section03Mono;

import com.github.javafaker.Faker;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

public class Lec18_UseOfFaker_Test {

    @Test
    void demo() {
        //when
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Faker.instance().name().fullName())
                //then
                .forEach(System.out::println);
    }

    @Test
    void utilDemo() {
        //when
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Util.FAKER.address().fullAddress())
                //then
                .forEach(System.out::println);
    }
}
