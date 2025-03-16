package com.example.order_service.repository;

import com.example.order_service.model.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @EntityGraph(attributePaths = "orderLineItems")
    @NonNull
    List<Order> findAll();
}
