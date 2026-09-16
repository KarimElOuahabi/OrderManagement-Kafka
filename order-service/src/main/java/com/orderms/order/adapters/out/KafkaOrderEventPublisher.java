package com.orderms.order.adapters.out;

import com.orderms.order.application.ports.OrderEventPublisher;
import com.orderms.order.domain.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final OrderEventMapper eventMapper;

    public KafkaOrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate, OrderEventMapper eventMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventMapper = eventMapper;
    }

    @Override
    public void publish(Order order) {
        OrderCreatedEvent event = eventMapper.toOrderCreatedEvent(order);
        kafkaTemplate.send("order.created", event);
    }
}
