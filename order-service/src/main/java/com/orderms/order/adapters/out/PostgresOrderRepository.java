package com.orderms.order.adapters.out;

import com.orderms.order.application.ports.OrderRepository;
import com.orderms.order.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostgresOrderRepository implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    public PostgresOrderRepository(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        Optional<OrderEntity> entity = jpaRepository.findById(orderId);
        return entity.map(mapper::toDomain);
    }
}
