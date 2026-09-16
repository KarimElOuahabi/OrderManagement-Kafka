package com.orderms.order.domain;
import java.math.BigDecimal;

public class Order {
    private String id;
    private OrderStatus status;
    private BigDecimal price;

    public Order(String id, OrderStatus status, BigDecimal price) {
        this.id = id;
        this.status = status;
        this.price = price;
    }

    public String getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getPrice() { return price; }
}


