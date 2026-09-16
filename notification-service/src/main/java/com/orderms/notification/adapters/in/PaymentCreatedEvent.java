package com.orderms.notification.adapters.in;

public record PaymentCreatedEvent(String orderId, String status) {}