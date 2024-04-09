package com.br.lanchonete.lanchoneteapi.dto;

import com.br.lanchonete.lanchoneteapi.model.Order;
import lombok.Data;

@Data
public class CloseOrderReturnDTO {
    private Double change;
    private Order order;
}
