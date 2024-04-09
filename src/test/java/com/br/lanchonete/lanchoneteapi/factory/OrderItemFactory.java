package com.br.lanchonete.lanchoneteapi.factory;

import com.br.lanchonete.lanchoneteapi.model.OrderItem;
import com.br.lanchonete.lanchoneteapi.model.Product;

import java.util.UUID;

public class OrderItemFactory {

    public static OrderItem createValidOrderItem(Product product, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setRelativePrice(product.getPrice() * quantity);
        return orderItem;
    }
}
