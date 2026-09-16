package com.orderms.notification.adapters.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "payment.completed", groupId = "notification-service")
    public void handle(String rawMessage) throws Exception {
        PaymentCreatedEvent event = objectMapper.readValue(rawMessage, PaymentCreatedEvent.class);
        String message = event.status().equals("COMPLETED")
                ? "Notification: Payment succeeded for order " + event.orderId()
                : "Notification: Payment failed for order " + event.orderId();
        System.out.println(message);
    }
}