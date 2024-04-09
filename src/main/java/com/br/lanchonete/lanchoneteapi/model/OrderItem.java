package com.br.lanchonete.lanchoneteapi.model;

import com.br.lanchonete.lanchoneteapi.model.abstracts.AbstractModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class OrderItem extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer quantity;
    private Double relativePrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
