package com.br.lanchonete.lanchoneteapi.controller;

import com.br.lanchonete.lanchoneteapi.dto.AddProductDTO;
import com.br.lanchonete.lanchoneteapi.dto.CreateOrderDTO;
import com.br.lanchonete.lanchoneteapi.dto.RemoveProductDTO;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrderReturnsCreatedStatus() throws Exception {
        // Arrange
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setClientId(UUID.randomUUID().toString());
        when(orderService.createOrder(any(CreateOrderDTO.class))).thenReturn(createOrderDTO);

        // Act and Assert
        mockMvc.perform(post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createOrderDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void createOrderThrowsExceptionReturnsBadRequest() throws Exception {
        // Arrange
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setClientId(null);

        // Act and Assert
        mockMvc.perform(post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createOrderDTO)))
                .andExpect(status().isBadRequest())
                        .andExpect(result -> {
                            String content = Objects.requireNonNull(result.getResolvedException()).getMessage();
                            Assertions.assertTrue(content.contains("Validation failed"), "The validation should fail with a message containing 'Validation failed'");
                        });

    }

    @Test
    void addProductReturnsUpdatedOrder() throws Exception {
        // Arrange
        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setOrderId(UUID.randomUUID().toString());
        addProductDTO.setProductId(UUID.randomUUID().toString());
        addProductDTO.setQuantity(1);

        when(orderService.addProduct(any(AddProductDTO.class))).thenReturn(addProductDTO);

        // Act and Assert
        mockMvc.perform(post("/orders/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addProductDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void removeProductReturnsUpdatedOrder() throws Exception {
        // Arrange
        RemoveProductDTO removeProductDTO = new RemoveProductDTO();
        removeProductDTO.setOrderId(UUID.randomUUID().toString());
        removeProductDTO.setProductId(UUID.randomUUID().toString());
        removeProductDTO.setQuantity(1);

        Order order = new Order();
        when(orderService.removeProduct(any(RemoveProductDTO.class))).thenReturn(order);

        // Act and Assert
        mockMvc.perform(post("/orders/remove-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(removeProductDTO)))
                .andExpect(status().isOk());
    }
}