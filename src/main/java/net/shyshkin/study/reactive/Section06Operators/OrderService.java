package net.shyshkin.study.reactive.Section06Operators;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderService {

    private static final int USER_ORDER_COUNT = 3;

    private static final Map<Integer, List<PurchaseOrder>> db = createDB();

    private static List<PurchaseOrder> createOrders(int userId) {
        return IntStream.rangeClosed(1, USER_ORDER_COUNT)
                .mapToObj(i -> new PurchaseOrder(userId))
                .collect(Collectors.toList());
    }

    private static Map<Integer, List<PurchaseOrder>> createDB() {
        return IntStream.rangeClosed(1, UserService.USERS_COUNT)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), OrderService::createOrders));
    }

    public static List<PurchaseOrder> getOrderList(int userId) {
        return db.get(userId);
    }

    public static Flux<PurchaseOrder> getOrders(int userId) {
        return Flux.fromIterable(db.get(userId))
                .delayElements(Duration.ofMillis(100));
    }
}
