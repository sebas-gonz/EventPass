package com.seb.eventpass.orders.controllers;

import com.seb.eventpass.orders.dto.requests.CreateOrderDTO;
import com.seb.eventpass.orders.dto.responses.OrderDTO;
import com.seb.eventpass.orders.services.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Mono<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO,
                                      Principal principal) {
        UUID userUuid = UUID.fromString(principal.getName());
        return orderService.createOrder(createOrderDTO, userUuid);
    }

    @GetMapping("/{orderuuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<OrderDTO> getOrder(@PathVariable UUID orderuuid) {
        return orderService.getOrderByUuid(orderuuid);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Flux<OrderDTO> getUserOrders(Principal principal) {
        UUID userUuid = UUID.fromString(principal.getName());
        return orderService.getOrdersByUserUuid(userUuid);
    }

}