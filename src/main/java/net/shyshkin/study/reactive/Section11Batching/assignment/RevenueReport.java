package net.shyshkin.study.reactive.Section11Batching.assignment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class RevenueReport {

    private final LocalDateTime date = LocalDateTime.now();
    private final Map<String, Double> revenue;
}
