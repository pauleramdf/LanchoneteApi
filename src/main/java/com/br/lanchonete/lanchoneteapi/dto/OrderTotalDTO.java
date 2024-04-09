package com.br.lanchonete.lanchoneteapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderTotalDTO {
    private String orderId;
    List<ProductOrderDTO> products;
}
