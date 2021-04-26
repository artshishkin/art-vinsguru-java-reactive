package net.shyshkin.study.reactive.Section14Context;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Lec167_Context_RateLimiterImplementation_Test {

    private BookService bookService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
    }

    @Test
    void getBook_once() {

        //when
        bookService
                .getBook()
                .contextWrite(userService.userCategoryContext())
                .contextWrite(Context.of("user", "art"))

                //then
                .subscribe(Util.subscriber());
    }

    @ParameterizedTest
    @CsvSource({
            "art,5,not-allowed",
            "kate,2,not-allowed",
            "arina,0,Unauthorized"
    })
    void getBook_multiple(String user, Integer expectSuccessCount, String expectErrorMessage) {

        //when
        Flux<String> flux = bookService
                .getBook()
                .repeat(20)
                .contextWrite(userService.userCategoryContext())
                .contextWrite(Context.of("user", user))

                .doOnNext(Util.onNext)
                .doOnError(Util.onError);

        //then
        StepVerifier.create(flux)
                .expectNextCount(expectSuccessCount)
                .verifyErrorSatisfies(
                        ex -> assertThat(ex)
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage(expectErrorMessage)
                );
    }
}
