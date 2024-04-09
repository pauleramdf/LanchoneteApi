package com.br.lanchonete.lanchoneteapi.repository;

import com.br.lanchonete.lanchoneteapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}
