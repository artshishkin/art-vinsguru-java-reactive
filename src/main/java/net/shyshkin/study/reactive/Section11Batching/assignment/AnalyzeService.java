package net.shyshkin.study.reactive.Section11Batching.assignment;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toMap;

public class AnalyzeService {

    private final AtomicReference<Double> revenue = new AtomicReference<>(0d);

    public Flux<RevenueReport> revenue(Flux<BookOrder> orderFlux, Flux<List<String>> genreFlux) {
        return Flux
                .combineLatest(orderFlux, genreFlux, (order, genres) -> {
                    return genres.contains(order.getGenre()) ? order : new BookOrder();
                })
                .filter(order -> order.getId() != null)
                .buffer(Duration.ofSeconds(2))
                .map(this::calculateRevenue);
    }

    private RevenueReport calculateRevenue(List<BookOrder> orders) {
        Map<String, Double> revenue = orders
                .stream()
                .collect(
                        toMap(
                                BookOrder::getGenre,
                                order -> order.getPrice() * order.getQuantity(),
                                Double::sum
                        )
                );
        return new RevenueReport(revenue);
    }
}
