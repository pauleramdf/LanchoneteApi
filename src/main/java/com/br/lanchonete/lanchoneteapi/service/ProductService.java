package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public CreateProductDTO createProduct(CreateProductDTO request) throws DefaultException{
        log.info("Creating product");

        if(validadeProduct(request)) {
            var product = request.toEntity();
            productRepository.save(product);
            return request;
        }
        throw new DefaultException("Product name already in use");
    }

    private boolean validadeProduct(CreateProductDTO request) {
        var product = this.productRepository.findByName(request.getName());
        return product.isEmpty();
    }
}
