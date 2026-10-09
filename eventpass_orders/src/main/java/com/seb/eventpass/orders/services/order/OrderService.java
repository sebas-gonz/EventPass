package com.seb.eventpass.orders.services.order;

import com.seb.eventpass.orders.dto.requests.CreateOrderDTO;
import com.seb.eventpass.orders.dto.responses.OrderDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {
    Mono<OrderDTO> createOrder(CreateOrderDTO createOrderDTO, UUID userUuid);
    Mono<OrderDTO> getOrderByUuid(UUID uuid);
    Flux<OrderDTO> getAllOrders();
    Mono<OrderDTO> approveOrder (UUID uuid, UUID paymentUuid);
    Mono<OrderDTO> rejectOrder (UUID uuid);
}
