package com.br.lanchonete.lanchoneteapi.controller;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.factory.ProductFactory;
import com.br.lanchonete.lanchoneteapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void createProductReturnsCreatedStatus() throws Exception {
        // Arrange
        CreateProductDTO createProductDTO = ProductFactory.createValidProductDTO();
        when(productService.createProduct(any(CreateProductDTO.class))).thenReturn(createProductDTO);

        // Act and Assert
        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createProductDTO.getName()))
                .andExpect(jsonPath("$.price").value(createProductDTO.getPrice()));
    }

    @Test
    void createProductThrowsExceptionReturnsBadRequest() throws Exception {
        // Arrange
        CreateProductDTO createProductDTO = ProductFactory.createValidProductDTO();
        when(productService.createProduct(any(CreateProductDTO.class))).thenThrow(new DefaultException("Product name already in use"));

        // Act
        MvcResult result = mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createProductDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String content = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(content.contains("Product name already in use"), "The exception message should be present in the response");
    }

    @Test
    void createProductThrowsExceptionInvalidBody() throws Exception {
        // Arrange
        CreateProductDTO createProductDTO = ProductFactory.createInvalidProduct();
        when(productService.createProduct(any(CreateProductDTO.class))).thenThrow(new DefaultException("Product name already in use"));

        // Act
        MvcResult result = mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createProductDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String content = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertTrue(content.contains("Validation failed"), "The validation should fail with a message containing 'Validation failed'");
    }
}
