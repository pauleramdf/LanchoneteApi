package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.AddProductDTO;
import com.br.lanchonete.lanchoneteapi.dto.CreateOrderDTO;
import com.br.lanchonete.lanchoneteapi.dto.RemoveProductDTO;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.OrderItem;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderEvents;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import com.br.lanchonete.lanchoneteapi.repository.ClientRepository;
import com.br.lanchonete.lanchoneteapi.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductService productService;
    private final OrderItemService orderItemService;

    public CreateOrderDTO createOrder(CreateOrderDTO request) throws DefaultException {
        log.info("Creating order");

        var client = clientRepository.findById(UUID.fromString(request.getClientId()))
                .orElseThrow(() -> new DefaultException("Client not found"));

        var order = new Order();
        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(0D);
        orderRepository.save(order);
        return request;
    }

    @Transactional
    public AddProductDTO addProduct(AddProductDTO request) throws DefaultException {
        log.info("Adding product to order");

        productService.validateProductQuantity(request);

        var order = orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new DefaultException("Order not found"));

        validateOrderActive(order);

        var product = productService.getProductById(UUID.fromString(request.getProductId()));

        var orderItem = orderItemService.createOrderItem(order, product, request.getQuantity());

        increaseOrderTotalPrice(order, orderItem.getRelativePrice());
        productService.decreaseProductQuantity(product, request.getQuantity());

      if (order.getStatus().equals(OrderStatus.PENDING)) {
            updateOrderStatus(order, OrderEvents.SUCCESS);
      }

        return request;
    }

    void validateOrderActive(Order order) throws DefaultException {
        if (!List.of(OrderStatus.PENDING, OrderStatus.IN_PROGRESS).contains(order.getStatus()) ) {
            throw new DefaultException("Order status is not active");
        }
    }

    public void increaseOrderTotalPrice(Order order, Double relativePrice) {
        log.debug("Increasing order {} total price {}", order.getId(), relativePrice);

        order.setTotalPrice(order.getTotalPrice() + relativePrice);
        orderRepository.save(order);
    }

    public void decreaseOrderTotalPrice(Order order, Double relativePrice) throws DefaultException {
        log.debug("Decreasing order {} total price {}", order.getId(), relativePrice);

        if (order.getTotalPrice() < relativePrice) {
            throw new DefaultException("Relative price is greater than total price in order");
        }
        order.setTotalPrice(order.getTotalPrice() - relativePrice);
        orderRepository.save(order);
    }


    public void updateOrderStatus(Order order, OrderEvents event) {
        log.debug("Updating order {} status to {}", order.getId(), event);

        order.setStatus(order.getStatus().nextState(event));
        orderRepository.save(order);
    }

    @Transactional
    public Order removeProduct(RemoveProductDTO request) throws DefaultException {
        log.info("Removing product from order");

        var order = orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new DefaultException("Order not found"));

        validateOrderActive(order);

        var product = productService.getProductById(UUID.fromString(request.getProductId()));

        var orderItem = orderItemService.findOrderItem(order, product);

        removeOrderItem(orderItem, request.getQuantity());

        decreaseOrderTotalPrice(order, request.getQuantity() * product.getPrice());

        productService.increaseProductQuantity(product, request.getQuantity());

        return order;
    }

    private void removeOrderItem(OrderItem orderItem, int quantityToRemove) throws DefaultException {
        orderItemService.validateOrderHasQuantity(orderItem, quantityToRemove);
        orderItemService.removeOrderItem(orderItem, quantityToRemove);
    }
}
