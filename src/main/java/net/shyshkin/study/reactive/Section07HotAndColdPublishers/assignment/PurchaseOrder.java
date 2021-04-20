package net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment;

import lombok.Data;
import lombok.SneakyThrows;
import net.shyshkin.study.reactive.courseutil.Util;

import java.text.NumberFormat;
import java.util.Locale;

@Data
public class PurchaseOrder {

    private String item;
    private double price;
    private String category;
    private int quantity;

    @SneakyThrows
    public PurchaseOrder() {
        this.item = Util.FAKER.commerce().productName();
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = format.parse(Util.FAKER.commerce().price());
        this.price = number.doubleValue();
        this.category = Util.FAKER.commerce().department();
        this.quantity = Util.FAKER.random().nextInt(1, 10);
    }
}
