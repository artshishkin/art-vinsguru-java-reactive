package net.shyshkin.study.reactive.Section11Batching.assignment;

import com.github.javafaker.Book;
import net.shyshkin.study.reactive.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

public class BookOrderService {

    public Flux<BookOrder> getOrders() {
        return Flux
                .interval(Duration.ofMillis(10))
                .map(i -> createRandomOrder());
    }

    private BookOrder createRandomOrder() {
        Book book = Util.FAKER.book();
        return BookOrder
                .builder()
                .id(UUID.randomUUID())
                .title(book.title())
                .genre(book.genre())
                .author(book.author())
                .quantity(Util.FAKER.random().nextInt(1, 5))
                .price(Util.FAKER.random().nextInt(10, 100))
                .build();
    }


}
