package com.seb.eventpass.orders.controllers;

import com.seb.eventpass.orders.dto.responses.OrderDTO;
import com.seb.eventpass.orders.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/internal/order")
@RequiredArgsConstructor
public class InternalOrderController {
    private final OrderService orderService;

    @PostMapping("/{orderuuid}/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<OrderDTO> approveOrder(@PathVariable UUID orderuuid,
                                       @RequestHeader(name = "X-PAYMENT-UUID") String paymentUuid) {
        return orderService.approveOrder(orderuuid, UUID.fromString(paymentUuid));
    }

    @PostMapping("/{orderuuid}/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<OrderDTO> rejectOrder(@PathVariable UUID orderuuid) {
        return orderService.rejectOrder(orderuuid);
    }
}
