package com.seb.eventpass.eventos.repositories;

import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.models.TicketCategory;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface TicketCategoryRepository extends ReactiveCrudRepository<TicketCategory, Long> {
    Flux<TicketCategory> findByEventId(Long eventId);

    Mono<TicketCategory> findByUuid(UUID uuid);

    Mono<Void> deleteByUuid(UUID uuid);

    @Modifying
    @Query(value = "UPDATE ticket_categories SET ticket_capacity = ticket_capacity - :quantity, version = version + 1 WHERE uuid = :ticketUuid " +
            "AND ticket_capacity >= :quantity")
    Mono<Integer> discountTicketCapacity(@Param("ticketUuid") UUID ticketUuid,@Param("quantity") Integer quantity);
}
