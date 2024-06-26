package com.br.lanchonete.lanchoneteapi.service;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.model.Product;
import com.br.lanchonete.lanchoneteapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public void validateProductQuantity(Product product, int quantity) throws DefaultException {
        log.debug("Validating product quantity in AddProductDTO");

        if (quantity > product.getQuantity()) {
            throw new DefaultException("Quantity in AddProductDTO is greater than available quantity in product");
        }
    }

    public Product getProductById(UUID uuid) throws DefaultException {
        return productRepository.findById(uuid)
                .orElseThrow(() -> new DefaultException("Product not found"));
    }

    public void decreaseProductQuantity(Product product, int quantity) throws DefaultException {
        log.debug("Decreasing product {} quantity {}", product, quantity);

        if(product.getQuantity() < quantity) {
            throw new DefaultException("Quantity is greater than available quantity in product");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public void increaseProductQuantity(Product product, int quantity) {
        log.debug("Increasing product {} quantity {}", product, quantity);

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }

    public List<Product> findAllInIds(List<String> list) {
        return productRepository.findAllById(list.stream().map(UUID::fromString).toList());
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
