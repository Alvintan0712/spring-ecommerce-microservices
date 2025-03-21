package com.example.order_service.service;

import com.example.order_service.dto.InventoryResponse;
import com.example.order_service.dto.OrderLineItemDto;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderLineItem;
import com.example.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestClient restClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDto()
                .stream()
                .map(this::mapOrderLineItem)
                .toList();
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(order);
        }

        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponses = restClient.get()
                .uri("http://localhost:8082/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .body(InventoryResponse[].class);

        boolean allProductsInStock = Arrays.stream(Objects.requireNonNull(inventoryResponses))
                .allMatch(InventoryResponse::isInStock);

        if (!allProductsInStock) {
            throw new IllegalArgumentException("Product is not in stock, please try again later.");
        }

        orderRepository.save(order);
    }

    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            System.out.println(order.getOrderLineItems().size());

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.getId());
            orderResponse.setOrderNumber(order.getOrderNumber());
            orderResponse.setOrderLineItemsDto(order.getOrderLineItems()
                    .stream()
                    .map(orderLineItem -> {
                        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
                        orderLineItemDto.setId(orderLineItem.getId());
                        orderLineItemDto.setSkuCode(orderLineItem.getSkuCode());
                        orderLineItemDto.setPrice(orderLineItem.getPrice());
                        orderLineItemDto.setQuantity(orderLineItem.getQuantity());
                        return orderLineItemDto;
                    }).toList());
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    private OrderLineItem mapOrderLineItem(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());

        return orderLineItem;
    }
}
