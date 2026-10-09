package com.seb.eventpass.ticket.repositories;

import com.seb.eventpass.ticket.models.Ticket;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Ticket, Long> {
    Mono<Ticket> findByUuid(UUID uuid);

    Flux<Ticket> findByUserUuid(UUID userUuid);
}
