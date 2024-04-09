package com.br.lanchonete.lanchoneteapi.factory;

import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.Product;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public class OrderFactory {

    public static Order createValidOrderEmpty() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(0D);
        order.setClient(ClientFactory.createValidClient());
        order.setItems(List.of());
        return order;
    }


    public static Order createValidOrder(Product product) {
        Order order = new Order();
        var quantity = 5;
        var orderItem = OrderItemFactory.createValidOrderItem(product, quantity);

        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(orderItem.getRelativePrice());
        order.setClient(ClientFactory.createValidClient());

        orderItem.setOrder(order);
        order.setItems(List.of(orderItem));
        return order;
    }
}
