package net.shyshkin.study.reactive.Section11Batching.assignment2;

import lombok.Data;
import net.shyshkin.study.reactive.courseutil.Util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

@Data
public class PurchaseOrder {

    private UUID orderId;
    private String item;
    private Double price;
    private String category;
    private int quantity;

    public PurchaseOrder() {
        this.orderId = UUID.randomUUID();

        this.item = Util.FAKER.commerce().productName();

        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = null;
        try {
            number = format.parse(Util.FAKER.commerce().price());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.price = number != null ? number.doubleValue() : 0;

        this.category = Util.FAKER.commerce().department();

        this.quantity = Util.FAKER.random().nextInt(1, 5);
    }
}
