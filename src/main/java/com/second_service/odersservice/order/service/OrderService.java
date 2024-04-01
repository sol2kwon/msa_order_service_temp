package com.second_service.odersservice.order.service;

import com.second_service.odersservice.order.dto.OrderDto;
import com.second_service.odersservice.order.repository.OrderEntity;

public interface OrderService {
    Iterable<OrderEntity> getOrdersByUserId(String userId);
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);

}
