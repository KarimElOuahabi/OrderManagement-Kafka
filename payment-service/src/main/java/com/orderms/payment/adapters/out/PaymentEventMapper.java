package com.orderms.payment.adapters.out;

import com.orderms.payment.domain.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventMapper {
    public PaymentCreatedEvent toPaymentCreatedEvent(Payment payment) {
        return new PaymentCreatedEvent(payment.getOrderId(), payment.getStatus().name());
    }
}