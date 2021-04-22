package net.shyshkin.study.reactive.Section11Batching.assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOrder {

    private UUID id;
    private String title;
    private String genre;
    private String author;
    private int quantity;
    private double price;
}
