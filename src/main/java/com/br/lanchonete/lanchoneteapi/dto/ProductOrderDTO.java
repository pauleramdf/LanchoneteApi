package com.br.lanchonete.lanchoneteapi.dto;

import lombok.Data;

@Data
public class ProductOrderDTO {
    private String productId;
    private int quantity;
}
