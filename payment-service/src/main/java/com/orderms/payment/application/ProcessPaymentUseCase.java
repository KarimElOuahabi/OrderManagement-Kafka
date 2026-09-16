package com.orderms.payment.application;

import com.orderms.payment.application.ports.PaymentEventPublisher;
import com.orderms.payment.application.ports.PaymentRepository;
import com.orderms.payment.domain.Payment;
import com.orderms.payment.domain.PaymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProcessPaymentUseCase {
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public ProcessPaymentUseCase(PaymentRepository paymentRepository, PaymentEventPublisher paymentEventPublisher) {
        this.paymentRepository = paymentRepository;
        this.paymentEventPublisher = paymentEventPublisher;
    }
    public void process(String orderId, BigDecimal price) {
        PaymentStatus status = simulateCharge(price);

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                orderId,
                price,
                status
        );

        paymentRepository.save(payment);
        paymentEventPublisher.publish(payment);


    }
    private PaymentStatus simulateCharge(BigDecimal price) {
        return Math.random() < 0.8 ? PaymentStatus.COMPLETED : PaymentStatus.FAILED;
    }
}
