package net.shyshkin.study.reactive.Section06Operators;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;

public class Lec76_Operator_FlatMap_Test {

    @Test
    void flatMap() {
        //given
        UserService.getUsers()

                //when
                .map(User::getUserId)
                .flatMap(OrderService::getOrders)

                //then
                .subscribe(Util.subscriber());
    }
}
