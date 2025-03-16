package com.example.order_service.repository;

import com.example.order_service.model.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderLineItem, UUID> {
    List<OrderLineItem> findByOrderId(UUID orderId);
}
