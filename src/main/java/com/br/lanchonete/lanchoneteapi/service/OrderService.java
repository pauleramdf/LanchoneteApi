package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.CreateOrderDTO;
import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import com.br.lanchonete.lanchoneteapi.repository.ClientRepository;
import com.br.lanchonete.lanchoneteapi.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public CreateOrderDTO createOrder(CreateOrderDTO request) throws DefaultException {
        log.info("Creating order");

        var client = clientRepository.findById(UUID.fromString(request.getClientId()))
                .orElseThrow(() -> new DefaultException("Client not found"));

        var order = new Order();
        order.setClient(client);
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);
        return request;
    }
}
