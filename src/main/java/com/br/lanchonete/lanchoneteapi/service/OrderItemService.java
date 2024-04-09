package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.OrderItem;
import com.br.lanchonete.lanchoneteapi.model.Product;
import com.br.lanchonete.lanchoneteapi.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItem createOrderItem(Order order, Product product, int quantity) {
        Optional<OrderItem> existingOrderItem = orderItemRepository.findByOrderAndProduct(order, product);

        if (existingOrderItem.isPresent()) {
            OrderItem orderItem = existingOrderItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + quantity);
            updateRelativePrice(orderItem);
            return orderItemRepository.save(orderItem);
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            updateRelativePrice(orderItem);
            return orderItemRepository.save(orderItem);
        }

    }

    private void updateRelativePrice(OrderItem orderItem) {
        orderItem.setRelativePrice(orderItem.getProduct().getPrice() * orderItem.getQuantity());
    }
}
