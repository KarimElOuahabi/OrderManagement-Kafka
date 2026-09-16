package com.orderms.payment.adapters.out;

public record PaymentCreatedEvent(String orderId, String status) {}