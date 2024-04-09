package com.br.lanchonete.lanchoneteapi.factory;

import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.model.Product;

import java.util.UUID;

public class ProductFactory {

    public static CreateProductDTO createValidProductDTO() {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Test Product");
        createProductDTO.setPrice(100.00);
        createProductDTO.setDescription("Product description");
        createProductDTO.setTag("Product tag");
        createProductDTO.setQuantity(10);
        return createProductDTO;
    }

    public static CreateProductDTO createInvalidProduct() {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        // Set invalid values for the product properties
        createProductDTO.setName(""); // Empty name
        createProductDTO.setPrice(-100.00); // Negative price
        createProductDTO.setDescription(""); // Empty description
        createProductDTO.setTag(""); // Empty tag
        createProductDTO.setQuantity(-10); // Negative quantity
        return createProductDTO;
    }

    public static Product createValidProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setPrice(100.00);
        product.setDescription("Product description");
        product.setTag("Product tag");
        product.setQuantity(10);
        return product;
    }
}
