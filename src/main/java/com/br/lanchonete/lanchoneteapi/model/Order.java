package com.br.lanchonete.lanchoneteapi.model;

import com.br.lanchonete.lanchoneteapi.model.abstracts.AbstractModel;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Order extends AbstractModel {
    @Id
    @GeneratedValue
    private UUID id;

    private OrderStatus status;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> items;
}
