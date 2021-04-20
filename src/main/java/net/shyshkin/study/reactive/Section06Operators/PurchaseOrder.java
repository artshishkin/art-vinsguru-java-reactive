package net.shyshkin.study.reactive.Section06Operators;

import lombok.Data;
import net.shyshkin.study.reactive.courseutil.Util;

@Data
public class PurchaseOrder {

    private String item;
    private String price;
    private int userId;

    public PurchaseOrder(int userId) {
        this.userId = userId;
        this.item = Util.FAKER.commerce().productName();
        this.price = Util.FAKER.commerce().price();
    }
}
