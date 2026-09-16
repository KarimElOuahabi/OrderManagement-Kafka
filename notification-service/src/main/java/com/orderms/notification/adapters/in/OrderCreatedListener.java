package com.orderms.notification.adapters.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order.created", groupId = "notification-service")
    public void handle(String rawMessage) throws Exception {
        OrderCreatedEvent event = objectMapper.readValue(rawMessage, OrderCreatedEvent.class);
        System.out.println("Notification: Order " + event.orderId() + " received, price: " + event.price());
    }
}