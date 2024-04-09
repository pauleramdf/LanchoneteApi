package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.factory.ProductFactory;
import com.br.lanchonete.lanchoneteapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProductSuccessfully() throws Exception {
        // Arrange
        CreateProductDTO createProductDTO = ProductFactory.createValidProductDTO();
        when(productRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(createProductDTO.toEntity());

        // Act
        CreateProductDTO result = productService.createProduct(createProductDTO);

        // Assert
        assertNotNull(result, "The created product should not be null");
        assertEquals(createProductDTO.getName(), result.getName(), "The product name should match the input");
        assertEquals(createProductDTO.getPrice(), result.getPrice(), "The product price should match the input");
    }

    @Test
    void createProductThrowsExceptionWhenNameInUse() {
        // Arrange
        CreateProductDTO createProductDTO = ProductFactory.createValidProductDTO();
        when(productRepository.findByName(any(String.class))).thenReturn(Optional.of(createProductDTO.toEntity()));

        // Act
        Exception exception = assertThrows(DefaultException.class, () -> productService.createProduct(createProductDTO));

        // Assert
        assertEquals("Product name already in use", exception.getMessage(), "The exception message should match the expected message");
        assertTrue(exception instanceof DefaultException, "The exception should be an instance of DefaultException");
    }
}
