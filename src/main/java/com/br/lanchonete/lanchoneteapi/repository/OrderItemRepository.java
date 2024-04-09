package com.br.lanchonete.lanchoneteapi.repository;

import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.model.OrderItem;
import com.br.lanchonete.lanchoneteapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>{

    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
}
