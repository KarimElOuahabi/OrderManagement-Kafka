package com.orderms.payment.adapters.in;

import com.orderms.payment.application.ProcessPaymentUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public OrderCreatedListener(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @KafkaListener(topics = "order.created", groupId = "payment-service")
    public void handle(OrderCreatedEvent event) {
        processPaymentUseCase.process(event.orderId(), event.price());
    }
}