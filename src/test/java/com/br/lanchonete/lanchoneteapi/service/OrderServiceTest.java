package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.CreateOrderDTO;
import com.br.lanchonete.lanchoneteapi.model.Client;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import com.br.lanchonete.lanchoneteapi.repository.ClientRepository;
import com.br.lanchonete.lanchoneteapi.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderSuccessfully() throws DefaultException {
        UUID clientId = UUID.randomUUID();
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setClientId(clientId.toString());

        Client client = new Client();
        Order order = new Order();
        order.setClient(client);
        order.setStatus(OrderStatus.IN_PROGRESS);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        CreateOrderDTO result = orderService.createOrder(createOrderDTO);

        assertEquals(clientId.toString(), result.getClientId());
        verify(clientRepository, times(1)).findById(clientId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrderClientNotFound() {
        UUID clientId = UUID.randomUUID();
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setClientId(clientId.toString());

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(DefaultException.class, () -> orderService.createOrder(createOrderDTO));
        verify(clientRepository, times(1)).findById(clientId);
        verify(orderRepository, times(0)).save(any(Order.class));
    }
}