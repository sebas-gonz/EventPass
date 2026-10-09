package com.seb.eventpass.eventos.controllers;

import com.seb.eventpass.eventos.dto.requests.CreateEventDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateEventDTO;
import com.seb.eventpass.eventos.dto.responses.EventDTO;
import com.seb.eventpass.eventos.services.event.EventService;
import lombok.RequiredArgsConstructor;
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
    public Mono<EventDTO> createEvent(@RequestBody CreateEventDTO createEventDTO) {
        return eventService.createEvent(createEventDTO);
    }

    @PutMapping("/{uuid}")
    public Mono<EventDTO> updateEvent(@PathVariable UUID uuid, @RequestBody UpdateEventDTO updateEventDTO) {
        return eventService.updateEvent(uuid, updateEventDTO);
    }

    @DeleteMapping("/{uuid}")
    public Mono<Void> deleteEvent(@PathVariable UUID uuid) {
        return eventService.deleteEvent(uuid);
    }

}
