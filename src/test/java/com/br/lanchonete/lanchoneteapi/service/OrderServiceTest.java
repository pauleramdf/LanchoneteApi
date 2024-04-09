package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.AddProductDTO;
import com.br.lanchonete.lanchoneteapi.dto.CreateOrderDTO;
import com.br.lanchonete.lanchoneteapi.factory.OrderFactory;
import com.br.lanchonete.lanchoneteapi.factory.OrderItemFactory;
import com.br.lanchonete.lanchoneteapi.factory.ProductFactory;
import com.br.lanchonete.lanchoneteapi.model.Client;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.Product;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderEvents;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import com.br.lanchonete.lanchoneteapi.repository.ClientRepository;
import com.br.lanchonete.lanchoneteapi.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemService orderItemService;

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

    @Test
    void testAddProduct() throws DefaultException {
        int quantity = 5;

        Order order = OrderFactory.createValidOrderEmpty();
        Product product = ProductFactory.createValidProduct();

        AddProductDTO request = new AddProductDTO();
        request.setOrderId(order.getId().toString());
        request.setProductId(product.getId().toString());
        request.setQuantity(quantity);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productService.getProductById(product.getId())).thenReturn(product);
        when(orderItemService.createOrderItem(order, product, quantity)).thenReturn(OrderItemFactory.createValidOrderItem(product, quantity));
        when(orderRepository.save(order)).thenReturn(order);

        AddProductDTO result = orderService.addProduct(request);

        verify(orderRepository, times(1)).findById(order.getId());
        verify(productService, times(1)).getProductById(product.getId());
        verify(orderRepository, times(2)).save(order);
        verify(productService, times(1)).decreaseProductQuantity(product, quantity);


        assertEquals(order.getId().toString(), result.getOrderId());
        assertEquals(product.getId().toString(), result.getProductId());
        assertEquals(quantity, result.getQuantity());
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        assertEquals(product.getPrice() * quantity, order.getTotalPrice());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class,  names = { "CANCELED", "DONE", "FAILED" })
    void testValidateOrderActive_OrderNotActive(OrderStatus orderStatus) {
        Order order = new Order();
        order.setStatus(orderStatus);

        assertThrows(DefaultException.class, () -> orderService.validateOrderActive(order));
    }


    @ParameterizedTest
    @EnumSource( value = OrderStatus.class, names = {"PENDING", "IN_PROGRESS"})
    void testValidateOrderActive_OrderIsActive(OrderStatus orderStatus) throws DefaultException {
        Order order = new Order();
        order.setStatus(orderStatus);

        orderService.validateOrderActive(order);
        assertDoesNotThrow(() -> orderService.validateOrderActive(order));
    }


    @Test
    void testIncreaseOrderTotalPrice() {
        Order order = new Order();
        order.setTotalPrice(100.0);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.increaseOrderTotalPrice(order, 20.0);

        verify(orderRepository, times(1)).save(order);
        assertEquals(120.0, order.getTotalPrice());
    }

    @Test
    void testDecreaseOrderTotalPrice() throws DefaultException {
        Order order = new Order();
        order.setTotalPrice(100.0);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.decreaseOrderTotalPrice(order, 20.0);

        verify(orderRepository, times(1)).save(order);
        assertEquals(80.0, order.getTotalPrice());
    }

    @Test
    void testDecreaseOrderTotalPrice_ThrowsException() {
        Order order = OrderFactory.createValidOrderEmpty();

        assertThrows(DefaultException.class, () -> orderService.decreaseOrderTotalPrice(order, 120.0));
    }

    @ParameterizedTest
    @CsvSource({
            "PENDING, SUCCESS, IN_PROGRESS",
            "PENDING, CANCEL, CANCELED",
            "PENDING, FAILED, FAILED",
            "IN_PROGRESS, SUCCESS, DONE",
            "IN_PROGRESS, CANCEL, CANCELED",
            "IN_PROGRESS, FAILED, FAILED",
            "DONE, SUCCESS, DONE",
            "CANCELED, SUCCESS, CANCELED",
            "FAILED, SUCCESS, FAILED"
    })
    void testUpdateOrderStatus(OrderStatus initialStatus, OrderEvents event, OrderStatus expectedStatus) {
        Order order = new Order();
        order.setStatus(initialStatus);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.updateOrderStatus(order, event);

        verify(orderRepository, times(1)).save(order);
        assertEquals(expectedStatus, order.getStatus());
    }
}
