package com.orderms.payment.adapters.out;

import com.orderms.payment.application.ports.PaymentRepository;
import com.orderms.payment.domain.Payment;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresPaymentRepository implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;

    public PostgresPaymentRepository(PaymentJpaRepository jpaRepository, PaymentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        PaymentEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}