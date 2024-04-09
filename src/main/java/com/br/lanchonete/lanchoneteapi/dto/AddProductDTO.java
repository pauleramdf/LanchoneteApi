package com.br.lanchonete.lanchoneteapi.dto;

import lombok.Data;

@Data
public class AddProductDTO {
    private String productId;
    private String orderId;
    private int quantity;
}
