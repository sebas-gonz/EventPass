package com.seb.eventpass.eventos.repositories;

import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.models.TicketCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface TicketCategoryRepository extends ReactiveCrudRepository<TicketCategory, Long> {
    Flux<TicketCategory> findByEventId(Long eventId);

    Mono<TicketCategory> findByUuid(UUID uuid);
}
