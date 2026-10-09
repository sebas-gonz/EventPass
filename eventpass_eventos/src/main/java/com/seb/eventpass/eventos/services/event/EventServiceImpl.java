package com.seb.eventpass.eventos.services.event;

import com.seb.eventpass.eventos.dto.requests.CreateEventDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateEventDTO;
import com.seb.eventpass.eventos.dto.responses.EventDTO;
import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.models.Event;
import com.seb.eventpass.eventos.models.TicketCategory;
import com.seb.eventpass.eventos.repositories.EventRepository;
import com.seb.eventpass.eventos.repositories.TicketCategoryRepository;
import com.seb.eventpass.eventos.services.ticketcategory.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final TicketCategoryService  ticketCategoryService;

    @Override
    @Transactional
    public Mono<EventDTO> createEvent(CreateEventDTO createEventDTO) {
        Event eventEntity = dtoToEntity(createEventDTO);
        return eventRepository.save(eventEntity)
                .map(createdEvent -> {
                    return this.entityToDTO(createdEvent, Collections.emptyList());
                });
    }

    @Override
    @Transactional
    public Mono<EventDTO> updateEvent(UUID eventId, UpdateEventDTO updateEventDTO) {
        return eventRepository.findByUuid(eventId)
                .flatMap(eventFound -> {
                    eventFound.setEventAddress(updateEventDTO.eventAddress());
                    eventFound.setEventName(updateEventDTO.eventName());
                    eventFound.setEventDescription(updateEventDTO.eventDescription());
                    eventFound.setEventDate(updateEventDTO.eventDate());
                    return eventRepository.save(eventFound)
                            .flatMap(updatedEvent -> {
                                return ticketCategoryService.getTicketsCategoryByEventUuid(updatedEvent.getUuid())
                                        .collectList()
                                        .map(ticketCategoryList -> this.entityToDTO(updatedEvent, ticketCategoryList));
                            });
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("No such event with id: " + eventId)));
    }

    @Override
    @Transactional
    public Mono<Void> deleteEvent(UUID eventId) {
        return eventRepository.deleteByUuid(eventId)
                .flatMap(affectedRows -> {
                    if (affectedRows > 0) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new NoSuchElementException("No such event with id: " + eventId));
                    }
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EventDTO> getEvent(UUID eventId) {
        return eventRepository.findByUuid(eventId)
                .flatMap(eventFound -> {
                    return ticketCategoryService.getTicketsCategoryByEventUuid(eventFound.getUuid())
                            .collectList()
                            .map(ticketCategoryList -> this.entityToDTO(eventFound, ticketCategoryList));
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("No such event with id: " + eventId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EventDTO> getEvents() {
        return eventRepository.findAll()
                .flatMap(event -> {
                    return ticketCategoryService.getTicketsCategoryByEventUuid(event.getUuid())
                            .collectList()
                            .map(ticketCategories -> this.entityToDTO(event, ticketCategories));
                });
    }

    private Event dtoToEntity(CreateEventDTO createEventDTO) {
        return Event.builder()
                .totalEventCapacity(0)
                .eventAddress(createEventDTO.eventAddress())
                .eventDate(createEventDTO.eventDate())
                .eventDescription(createEventDTO.eventDescription())
                .eventName(createEventDTO.eventName())
                .build();
    }


    private EventDTO entityToDTO(Event event, List<TicketCategoryDTO> ticketCategory) {
        return new EventDTO(event.getUuid(),
                event.getEventName(),
                event.getEventDescription(),
                event.getEventDate(),
                event.getEventAddress(),
                event.getTotalEventCapacity(),
                event.getCreatedAt(),
                new HashSet<>(ticketCategory)
                );
    }
}
