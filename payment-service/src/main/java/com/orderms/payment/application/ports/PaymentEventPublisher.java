package com.orderms.payment.application.ports;

import com.orderms.payment.domain.Payment;

public interface PaymentEventPublisher {
    void publish(Payment payment);
}