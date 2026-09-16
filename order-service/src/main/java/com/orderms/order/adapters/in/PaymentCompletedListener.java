package com.orderms.order.adapters.in;

import com.orderms.order.application.ports.OrderRepository;
import com.orderms.order.domain.Order;
import com.orderms.order.domain.OrderStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentCompletedListener {

    private final OrderRepository orderRepository;

    public PaymentCompletedListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "payment.completed", groupId = "order-service")
    public void handle(PaymentCreatedEvent event) {
        Optional<Order> maybeOrder = orderRepository.findById(event.orderId());

        if (maybeOrder.isEmpty()) {
            return; // logging can be added later
        }

        Order existingOrder = maybeOrder.get();
        OrderStatus newStatus = event.status().equals("COMPLETED")
                ? OrderStatus.PAID
                : OrderStatus.PAYMENT_FAILED;
        Order updatedOrder = new Order(existingOrder.getId(), newStatus, existingOrder.getPrice());
        orderRepository.save(updatedOrder);    }
}