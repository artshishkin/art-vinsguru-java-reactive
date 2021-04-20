package net.shyshkin.study.reactive.Section07HotAndColdPublishers;

import net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment.InventoryService;
import net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment.OrderService;
import net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment.PurchaseOrder;
import net.shyshkin.study.reactive.Section07HotAndColdPublishers.assignment.RevenueService;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Lec87_ASSIGNMENT_InventoryRevenueStream_Test {


    @Test
    void assignment() {
        //given
        OrderService orderService = new OrderService();
        RevenueService revenueService = new RevenueService();
        InventoryService inventoryService = new InventoryService();

        Flux<PurchaseOrder> orderStream = orderService.orderStream();

        orderStream.subscribe(inventoryService.subscribeOrderStream());
        orderStream.subscribe(revenueService.subscribeOrderStream());

        //when
        inventoryService.inventoryStream()
                .subscribe(Util.subscriber("Inventory client"));
        revenueService.revenueStream()
                .subscribe(Util.subscriber("Revenue client"));

        //then
        Util.sleep(10);

    }
}
