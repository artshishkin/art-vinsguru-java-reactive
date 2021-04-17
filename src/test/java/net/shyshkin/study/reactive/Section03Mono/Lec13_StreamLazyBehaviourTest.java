package net.shyshkin.study.reactive.Section03Mono;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Lec13_StreamLazyBehaviourTest {

    @Test
    void noTerminalOperator() {
        //given
        Stream<Integer> stream = Stream.of(1)
                .map(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i * 2;
                });

        //when
        System.out.println(stream);

        //then
        System.out.println("Stream was not executed");

    }

    @Test
    void withTerminalOperator() {
        //given
        Stream<Integer> stream = Stream.of(1)
                .map(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i * 2;
                });

        //when
        stream.forEach(System.out::println);

        //then
        System.out.println("Stream was executed by terminal operator");
    }
}