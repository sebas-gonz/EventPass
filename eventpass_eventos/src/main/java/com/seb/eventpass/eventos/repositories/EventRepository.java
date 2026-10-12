package com.seb.eventpass.eventos.repositories;

import com.seb.eventpass.eventos.dto.responses.EventDTO;
import com.seb.eventpass.eventos.models.Event;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EventRepository extends ReactiveCrudRepository<Event, Long> {
    Mono<Event> findByUuid(UUID uuid);

    Mono<Integer> deleteByUuid(UUID uuid);

    @Modifying
    @Query(value = "UPDATE events SET total_event_capacity = total_event_capacity - :quantity, version = version + 1 WHERE id = :eventId AND total_event_capacity >= :quantity")
    Mono<Integer> discountEventCapacity(@Param("eventId") Long eventId, @Param("quantity") Integer quantity);
}
