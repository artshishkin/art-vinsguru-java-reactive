package net.shyshkin.study.reactive.Section06Operators;

import reactor.core.publisher.Flux;

public class UserService {

    static final int USERS_COUNT = 4;

    public static Flux<User> getUsers() {

        return Flux.range(1, USERS_COUNT)
                .map(User::new);
    }
}
