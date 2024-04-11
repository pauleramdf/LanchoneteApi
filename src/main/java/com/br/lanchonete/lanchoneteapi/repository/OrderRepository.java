package com.br.lanchonete.lanchoneteapi.repository;

import com.br.lanchonete.lanchoneteapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByOrderId(UUID id);
}
