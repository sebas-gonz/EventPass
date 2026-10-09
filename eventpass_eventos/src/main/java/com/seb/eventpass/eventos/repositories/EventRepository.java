package com.seb.eventpass.eventos.repositories;

import com.seb.eventpass.eventos.dto.responses.EventDTO;
import com.seb.eventpass.eventos.models.Event;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EventRepository extends ReactiveCrudRepository<Event, Long> {
    Mono<Event> findByUuid(UUID uuid);

    Mono<Integer> deleteByUuid(UUID uuid);
}
