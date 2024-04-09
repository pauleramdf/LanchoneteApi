package com.br.lanchonete.lanchoneteapi.dto;

import lombok.Data;

@Data
public class CloseOrderDTO {
    private String orderId;
    private Double paymentValue;
}
