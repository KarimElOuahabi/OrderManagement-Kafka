package com.orderms.order.adapters.out;

import java.math.BigDecimal;

public record OrderCreatedEvent(String orderId, BigDecimal price) {}
