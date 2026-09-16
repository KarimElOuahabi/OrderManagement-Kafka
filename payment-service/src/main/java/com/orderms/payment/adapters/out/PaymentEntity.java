package com.orderms.payment.adapters.out;

import com.orderms.payment.domain.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private String id;

    private String orderId;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    protected PaymentEntity() {}

    public PaymentEntity(String id, String orderId, BigDecimal price, PaymentStatus status) {
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