package com.orderms.payment.domain;

import java.math.BigDecimal;

public class Payment {
    private String id;
    private String orderId;
    private BigDecimal price;
    private PaymentStatus status;

    public Payment(String id, String orderId, BigDecimal price, PaymentStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.price = price;
        this.status = status;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public BigDecimal getPrice() { return price; }
    public PaymentStatus getStatus() { return status; }
}