package com.orderms.order.adapters.out;

import com.orderms.order.domain.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal price;

    // JPA requires a no-args constructor — Spring/Hibernate uses it internally
    protected OrderEntity() {}

    public OrderEntity(String id, OrderStatus status, BigDecimal price) {
        this.id = id;
        this.status = status;
        this.price = price;
    }

    // getters needed for JPA/Hibernate to read the fields
    public String getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getPrice() { return price; }
}
