package com.orderms.order.application.ports;

import com.orderms.order.domain.Order;

public interface OrderEventPublisher {
    void publish(Order order);
}
