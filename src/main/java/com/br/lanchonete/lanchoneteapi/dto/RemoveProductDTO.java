package com.br.lanchonete.lanchoneteapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveProductDTO {
    @NotNull
    private String productId;
    @NotNull
    private String orderId;
    @Min(1)
    private int quantity;
}
