package com.orderms.payment.adapters.in;

import java.math.BigDecimal;

public record OrderCreatedEvent(String orderId, BigDecimal price) {}