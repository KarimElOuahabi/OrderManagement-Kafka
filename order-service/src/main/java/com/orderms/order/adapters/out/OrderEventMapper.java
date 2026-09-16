package com.orderms.order.adapters.out;

import com.orderms.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderEventMapper {
    public OrderCreatedEvent toOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(order.getId(), order.getPrice());
    }
}