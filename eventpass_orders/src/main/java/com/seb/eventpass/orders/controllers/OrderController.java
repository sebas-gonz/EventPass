package com.seb.eventpass.orders.controllers;

import com.seb.eventpass.orders.dto.requests.CreateOrderDTO;
import com.seb.eventpass.orders.dto.responses.OrderDTO;
import com.seb.eventpass.orders.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderDTO> createOrder(@RequestBody CreateOrderDTO createOrderDTO,
                                      @RequestHeader(required = true, name = "X-USER-UUID")UUID userUuid) {
        return orderService.createOrder(createOrderDTO, userUuid);
    }

    @GetMapping("/{orderuuid}")
    public Mono<OrderDTO> getOrder(@PathVariable UUID orderuuid) {
        return orderService.getOrderByUuid(orderuuid);
    }
    @GetMapping
    public Flux<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/{orderuuid}/approved")
    public Mono<OrderDTO> approveOrder(@PathVariable UUID orderuuid,
                                       @RequestHeader(name = "X-PAYMENT-UUID") String paymentUuid) {
        return orderService.approveOrder(orderuuid, UUID.fromString(paymentUuid));
    }

    @PostMapping("/{orderuuid}/rejected")
    public Mono<OrderDTO> rejectOrder(@PathVariable UUID orderuuid) {
        return orderService.rejectOrder(orderuuid);
    }
}