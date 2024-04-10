package com.br.lanchonete.lanchoneteapi.model;

import com.br.lanchonete.lanchoneteapi.model.abstracts.AbstractModel;
import com.br.lanchonete.lanchoneteapi.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order extends AbstractModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("order")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> items;
}
