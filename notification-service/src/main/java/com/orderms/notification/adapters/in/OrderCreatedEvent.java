package com.orderms.notification.adapters.in;

public record OrderCreatedEvent(String orderId, java.math.BigDecimal price) {}