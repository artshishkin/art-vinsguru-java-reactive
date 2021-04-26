package net.shyshkin.study.reactive.Section14Context;

import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BookService {

    private final Map<String, Integer> ALLOWED_ATTEMPTS_COUNT = new HashMap<>(Map.of(
            "std", 2,
            "prime", 5
    ));

    public Mono<String> getBook() {
        return Mono
                .deferContextual(ctx -> ctx.get("allow") ?
                        Mono.just(Util.FAKER.book().title()) :
                        Mono.error(new RuntimeException("not-allowed"))
                )
                .contextWrite(rateLimiterContext());
    }

    Function<Context, Context> rateLimiterContext() {
        return ctx -> ctx
                .getOrEmpty("category")
                .map(String::valueOf)
                .map(category -> ALLOWED_ATTEMPTS_COUNT.computeIfPresent(category, (cat, count) -> Math.max(count - 1, -1)))
                .map(attempts -> attempts > -1)
                .map(allow -> ctx.put("allow", allow))
                .orElseThrow(() -> new RuntimeException("Category absent"));
    }
}
