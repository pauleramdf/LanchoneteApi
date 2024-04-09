package com.br.lanchonete.lanchoneteapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderDTO {
    @NotNull
    private String clientId;
}
