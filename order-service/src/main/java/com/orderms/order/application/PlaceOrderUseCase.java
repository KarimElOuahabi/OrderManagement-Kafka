package com.orderms.order.application;

import com.orderms.order.application.ports.OrderEventPublisher;
import com.orderms.order.application.ports.OrderRepository;
import com.orderms.order.domain.Order;
import com.orderms.order.domain.OrderStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PlaceOrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public PlaceOrderUseCase(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    public Order placeOrder(BigDecimal price) {
        Order order = new Order(
                UUID.randomUUID().toString(),
                OrderStatus.PENDING,
                price
        );

        orderRepository.save(order);
        orderEventPublisher.publish(order);

        return order;
    }
}
