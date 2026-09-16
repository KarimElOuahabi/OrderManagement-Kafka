package com.orderms.payment.application.ports;

import com.orderms.payment.domain.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}