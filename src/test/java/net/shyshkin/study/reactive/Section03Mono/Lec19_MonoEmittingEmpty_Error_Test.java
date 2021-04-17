package net.shyshkin.study.reactive.Section03Mono;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec19_MonoEmittingEmpty_Error_Test {

    private static UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        userRepository = new UserRepository();
    }

    @Test
    void getUserPresent() {
        //given
        int userId = 1;

        //when
        Mono<String> mono = userRepository
                .findById(userId)
                .doOnNext(Util.onNext);

        //then
        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getUserAbsent() {
        //given
        int userId = 2;

        //when
        Mono<String> mono = userRepository
                .findById(userId)
                .doOnNext(Util.onNext);

        //then
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    void getUserError() {
        //given
        int userId = -1;

        //when
        Mono<String> mono = userRepository
                .findById(userId)
                .doOnNext(Util.onNext)
                .doOnError(Util.onError);

        //then
        StepVerifier.create(mono)
                .verifyErrorMessage("Out of range");
    }

    @RepeatedTest(value = 3, name = "userId = {currentRepetition}")
    void getUserError_subscribe(RepetitionInfo repetitionInfo) {
        //given
        int userId = repetitionInfo.getCurrentRepetition();

        //when
        Mono<String> mono = userRepository
                .findById(userId);

        //then
        mono.subscribe(Util.onNext, Util.onError, Util.onComplete);
    }

    private static class UserRepository {

        public Mono<String> findById(int id) {
            return id == 1 ? Mono.just(Util.FAKER.name().name()) :
                    id == 2 ? Mono.empty() :
                            Mono.error(new RuntimeException("Out of range"));
        }
    }

}
