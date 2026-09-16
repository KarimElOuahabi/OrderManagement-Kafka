    package com.orderms.payment.adapters.out;

    import com.orderms.payment.domain.Payment;
    import org.springframework.stereotype.Component;

    @Component
    public class PaymentMapper {
        public PaymentEntity toEntity(Payment payment) {
            return new PaymentEntity(payment.getId(), payment.getOrderId(), payment.getPrice(), payment.getStatus());
        }
        public Payment toDomain(PaymentEntity paymentEntity) {
            return new Payment(paymentEntity.getId(), paymentEntity.getOrderId(), paymentEntity.getPrice(), paymentEntity.getStatus());
        }
    }
