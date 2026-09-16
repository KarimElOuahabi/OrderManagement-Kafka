package com.orderms.order.adapters.out;

import com.orderms.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderEntity toEntity(Order order) {
        return new OrderEntity(order.getId(), order.getStatus(), order.getPrice());
    }

    public Order toDomain(OrderEntity entity) {
        return new Order(entity.getId(), entity.getStatus(), entity.getPrice());
    }
};
