package com.seb.eventpass.eventos.services.event;

import com.seb.eventpass.eventos.dto.requests.CreateEventDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateEventDTO;
import com.seb.eventpass.eventos.dto.responses.EventDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EventService {
    Mono<EventDTO> createEvent(CreateEventDTO createEventDTO);
    Mono<EventDTO> updateEvent(UUID eventId,UpdateEventDTO updateEventDTO);
    Mono<Void> deleteEvent(UUID eventId);
    Mono<EventDTO> getEvent(UUID eventId);
    Flux<EventDTO> getEvents();
}
