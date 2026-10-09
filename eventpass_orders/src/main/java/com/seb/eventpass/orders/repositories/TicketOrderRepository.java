package com.seb.eventpass.orders.repositories;

import com.seb.eventpass.orders.models.TicketOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface TicketOrderRepository extends ReactiveCrudRepository<TicketOrder, Long> {
    Mono<TicketOrder> findByUuid(UUID uuid);

    Flux<TicketOrder> findByOrderId(Long orderId);
}
