package com.orderms.order.adapters.in;

public record PaymentCreatedEvent(String orderId, String status) {}