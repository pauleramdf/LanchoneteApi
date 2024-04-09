package com.br.lanchonete.lanchoneteapi.dto;

import com.br.lanchonete.lanchoneteapi.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    private Double price;
    @Min(0)
    private Integer quantity;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private String tag;


    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .description(description)
                .tag(tag)
                .build();
    }
}
