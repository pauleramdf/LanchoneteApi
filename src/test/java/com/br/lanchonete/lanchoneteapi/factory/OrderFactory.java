package com.br.lanchonete.lanchoneteapi.factory;

import com.br.lanchonete.lanchoneteapi.model.Order;
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
}
