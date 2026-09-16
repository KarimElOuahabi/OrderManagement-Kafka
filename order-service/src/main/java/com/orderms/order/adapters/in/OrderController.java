package com.orderms.order.adapters.in;

import com.orderms.order.adapters.in.dto.PlaceOrderRequest;
import com.orderms.order.application.PlaceOrderUseCase;
import com.orderms.order.application.ports.OrderEventPublisher;
import com.orderms.order.domain.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    public Order placeOrder(@RequestBody PlaceOrderRequest request) {
        return placeOrderUseCase.placeOrder(request.price());
    }
}
