package net.shyshkin.study.reactive.Section04Flux;

import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NameGenerator {

    public static List<String> generateNameList(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> getName())
                .collect(Collectors.toList());
    }

    public static Flux<String> generateNameFlux(int count) {
        return Flux.range(0, count)
                .map(i -> getName());
    }

    private static String getName() {
        Util.sleep(1);
        return Util.FAKER.name().fullName();
    }

}
