package net.shyshkin.study.reactive.Section14Context;

import reactor.util.context.Context;

import java.util.Map;
import java.util.function.Function;

public class UserService {

    private final Map<String, String> DB = Map.of(
            "art", "prime",
            "kate", "std"
    );

    public Function<Context, Context> userCategoryContext() {
        return ctx -> ctx
                .getOrEmpty("user")
                .map(String::valueOf)
                .map(DB::get)
                .map(category -> ctx.put("category", category))
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
    }

}
