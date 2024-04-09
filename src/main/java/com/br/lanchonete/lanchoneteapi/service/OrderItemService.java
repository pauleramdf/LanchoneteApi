package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.OrderItem;
import com.br.lanchonete.lanchoneteapi.model.Product;
import com.br.lanchonete.lanchoneteapi.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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

    public OrderItem findOrderItem(Order order, Product product) {
        return orderItemRepository.findByOrderAndProduct(order, product)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
    }

    public void removeOrderItem(OrderItem orderItem, int quantityToRemove) {
        log.info("Removing product from order {} quantity {}", orderItem.getId(), quantityToRemove);
        int newQuantity = orderItem.getQuantity() - quantityToRemove;
        orderItem.setQuantity(newQuantity);

        updateRelativePrice(orderItem);

        if (newQuantity == 0) {
            orderItemRepository.delete(orderItem);
        } else {
            orderItemRepository.save(orderItem);
        }
    }

    public void validateOrderHasQuantity(OrderItem orderItem, int quantity) throws DefaultException {
        if (orderItem.getQuantity() < quantity) {
            throw new DefaultException("Order item has less quantity than requested");
        }
    }
}
