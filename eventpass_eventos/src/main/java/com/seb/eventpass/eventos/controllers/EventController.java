package com.seb.eventpass.eventos.controllers;

import com.seb.eventpass.eventos.dto.requests.CreateEventDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateEventDTO;
import com.seb.eventpass.eventos.dto.responses.EventDTO;
import com.seb.eventpass.eventos.services.event.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Flux<EventDTO> getAllEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/{uuid}")
    public Mono<EventDTO> getEvent(@PathVariable UUID uuid) {
        return eventService.getEvent(uuid);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<EventDTO> createEvent(@Valid @RequestBody CreateEventDTO createEventDTO) {
        return eventService.createEvent(createEventDTO);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<EventDTO> updateEvent(@PathVariable UUID uuid,@Valid @RequestBody UpdateEventDTO updateEventDTO) {
        return eventService.updateEvent(uuid, updateEventDTO);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> deleteEvent(@PathVariable UUID uuid) {
        return eventService.deleteEvent(uuid);
    }

}
