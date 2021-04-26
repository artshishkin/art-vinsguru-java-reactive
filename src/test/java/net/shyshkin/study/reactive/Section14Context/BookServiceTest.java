package net.shyshkin.study.reactive.Section14Context;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookServiceTest {

    private static BookService bookService;

    @BeforeAll
    static void beforeAll() {
        bookService = new BookService();
    }

    @ParameterizedTest
    @CsvSource({
            "prime,5",
            "std,2"
    })
    void rateLimiterContext_present(String category, int expectedAllowCount) {

        //given
        boolean allow;
        int allowCount = -1;

        //when
        do {
            Context context = bookService.rateLimiterContext().apply(Context.of("category", category));
            allow = context.get("allow");
            allowCount++;
        } while (allow);

        //then
        assertThat(allowCount)
                .isEqualTo(expectedAllowCount);
    }

    @Test
    void rateLimiterContext_categoryAbsent() {

        //given
        String category = "Absent";

        //when
        ThrowableAssert.ThrowingCallable exec = ()->{
            Context context = bookService.rateLimiterContext().apply(Context.of("category", category));
        };

        //then
        assertThatThrownBy(exec)
                .isInstanceOf(RuntimeException.class)
        .hasMessage("Category absent");
    }

    @Test
    void rateLimiterContext_categoryNotProvided() {

        //when
        ThrowableAssert.ThrowingCallable exec = ()->{
            Context context = bookService.rateLimiterContext().apply(Context.empty());
        };

        //then
        assertThatThrownBy(exec)
                .isInstanceOf(RuntimeException.class)
        .hasMessage("Category absent");
    }
}