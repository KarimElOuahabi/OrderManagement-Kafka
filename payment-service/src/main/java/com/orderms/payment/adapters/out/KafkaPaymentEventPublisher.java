package com.orderms.payment.adapters.out;

import com.orderms.payment.application.ports.PaymentEventPublisher;
import com.orderms.payment.domain.Payment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private final KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate;
    private final PaymentEventMapper eventMapper;

    public KafkaPaymentEventPublisher(KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate, PaymentEventMapper eventMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventMapper = eventMapper;
    }

    @Override
    public void publish(Payment payment) {
        PaymentCreatedEvent event = eventMapper.toPaymentCreatedEvent(payment);
        kafkaTemplate.send("payment.completed", event);
    }
}