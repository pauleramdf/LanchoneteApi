package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.*;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.OrderItem;
import com.br.lanchonete.lanchoneteapi.model.Product;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderEvents;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import com.br.lanchonete.lanchoneteapi.repository.ClientRepository;
import com.br.lanchonete.lanchoneteapi.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Order createOrder() throws DefaultException {
        log.info("Creating order");

//        var client = clientRepository.findById(UUID.fromString(request.getClientId()))
//                .orElseThrow(() -> new DefaultException("Client not found"));

        var order = new Order();
//        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(0D);
        return orderRepository.save(order);
    }


    @Transactional
    public AddProductDTO addProduct(AddProductDTO request) throws DefaultException {
        log.info("Adding product to order");

        var order = findById(UUID.fromString(request.getOrderId()));
        validateOrderActive(order);

        if (order.getStatus().equals(OrderStatus.PENDING)) {
            updateOrderStatus(order, OrderEvents.SUCCESS);
        }

        var product = productService.getProductById(UUID.fromString(request.getProductId()));
        addProductToOrder(order, product, request.getQuantity());

        return request;
    }

    @Transactional
    public void addProductToOrder(Order order, Product product, int quantity) throws DefaultException {

        productService.validateProductQuantity(product, quantity);
        var orderItem = orderItemService.createOrderItem(order, product, quantity);

        increaseOrderTotalPrice(order, orderItem.getRelativePrice());
        productService.decreaseProductQuantity(product, quantity);

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

        var order = findById(UUID.fromString(request.getOrderId()));

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

    public Order getTotalPrice(String orderId) throws DefaultException {
        var order = findById(UUID.fromString(orderId));

        order.getItems().stream().map(OrderItem::getRelativePrice).reduce(Double::sum)
                .ifPresent(order::setTotalPrice);

        return orderRepository.save(order);
    }

    private Order findById(UUID orderId) throws DefaultException {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DefaultException("Order not found"));
    }

    public CloseOrderReturnDTO closeOrder(CloseOrderDTO request) throws DefaultException {
        log.info("Closing order");

        var order = findById(UUID.fromString(request.getOrderId()));

        validateOrderActive(order);
        validatePaymentValue(order, request.getPaymentValue());

        order.setStatus(order.getStatus().nextState(OrderEvents.SUCCESS));

        var change = request.getPaymentValue() - order.getTotalPrice();

        orderRepository.save(order);


        var returnDTO = new CloseOrderReturnDTO();
        returnDTO.setOrder(order);
        returnDTO.setChange(change);

        return returnDTO;
    }

    private void validatePaymentValue(Order order, Double paymentValue) throws DefaultException {
        if (order.getTotalPrice() == 0) {
            throw new DefaultException("Order total price is 0");
        }

        if (order.getTotalPrice() > paymentValue){
            order.setStatus(order.getStatus().nextState(OrderEvents.FAILED));
            throw new DefaultException("Payment value is less than total price");
        }

    }

    @Transactional
    public Order getTotalPriceBatch(OrderTotalDTO request) throws DefaultException {
        log.info("Getting total price of order batch");

        Order order = null;

        if (request.getOrderId() == null) {
            order = createOrder();
        } else {
             order = findById(UUID.fromString(request.getOrderId()));
        }

        if (order.getStatus().equals(OrderStatus.PENDING)) {
            updateOrderStatus(order, OrderEvents.SUCCESS);
        }

        var products = productService.findAllInIds(request.getProducts().stream().map(ProductOrderDTO::getProductId).toList());


        for (ProductOrderDTO productOrderDTO : request.getProducts()) {
            var product = products.stream()
                    .filter(p -> p.getId().equals(UUID.fromString(productOrderDTO.getProductId())))
                    .findFirst()
                    .orElseThrow(() -> new DefaultException("Product not found in request"));

            addProductToOrder(order, product, productOrderDTO.getQuantity());
        }

        return order;
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
