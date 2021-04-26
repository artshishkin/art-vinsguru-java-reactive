package net.shyshkin.study.reactive.Section14Context;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {

    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userService = new UserService();
    }

    @ParameterizedTest
    @CsvSource({
            "art,prime",
            "kate,std"
    })
    void userCategoryContext_present(String user, String category) {

        //when
        Context context = userService.userCategoryContext().apply(Context.of("user", user));

        //then
        assertEquals(category, context.get("category"));
    }

    @Test
    void userCategoryContext_absentUser() {

        //given
        String user = "Absent User";

        //when
        ThrowableAssert.ThrowingCallable exec = () -> {
            Context context = userService.userCategoryContext().apply(Context.of("user", user));
        };

        //then
        assertThatThrownBy(exec)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unauthorized");
    }

    @Test
    void userCategoryContext_userNotProvided() {

        //when
        ThrowableAssert.ThrowingCallable exec = () -> {
            Context context = userService.userCategoryContext().apply(Context.empty());
        };

        //then
        assertThatThrownBy(exec)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unauthorized");
    }


}