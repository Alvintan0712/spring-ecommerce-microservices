package com.example.order_service.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private String orderNumber;
    private List<OrderLineItemDto> orderLineItemsDto;
}
