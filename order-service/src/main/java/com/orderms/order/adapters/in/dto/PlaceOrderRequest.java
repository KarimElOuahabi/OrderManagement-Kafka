package com.orderms.order.adapters.in.dto;

import java.math.BigDecimal;

public record PlaceOrderRequest(
        BigDecimal price
) {};
