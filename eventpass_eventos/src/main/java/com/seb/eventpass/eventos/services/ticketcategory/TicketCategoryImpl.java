package com.seb.eventpass.eventos.services.ticketcategory;

import com.seb.eventpass.eventos.dto.requests.CreateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.models.Event;
import com.seb.eventpass.eventos.models.TicketCategory;
import com.seb.eventpass.eventos.models.TicketsCategories;
import com.seb.eventpass.eventos.repositories.EventRepository;
import com.seb.eventpass.eventos.repositories.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TicketCategoryImpl implements TicketCategoryService {
    private final TicketCategoryRepository ticketCategoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Mono<TicketCategoryDTO> createTicketCategory(CreateTicketCategoryDTO createTicketCategoryDTO) {
        return eventRepository.findByUuid(createTicketCategoryDTO.eventUuid())
                .flatMap(eventFound -> {
                    eventFound.setTotalEventCapacity(eventFound.getTotalEventCapacity() + createTicketCategoryDTO.ticketCapacity());
                    Mono<Event> updatedEvent = eventRepository.save(eventFound);

                    TicketCategory ticketCategory = this.dtoToEntity(createTicketCategoryDTO, eventFound.getId());
                    Mono<TicketCategory> createdTicketCategory = ticketCategoryRepository.save(ticketCategory);

                    return Mono.zip(updatedEvent, createdTicketCategory)
                            .map(tuple -> entityToDTO(tuple.getT2()));
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("No Event found with id: " + createTicketCategoryDTO.eventUuid())));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TicketCategoryDTO> getTicketsCategoryByEventUuid(UUID eventUuid) {
        return eventRepository.findByUuid(eventUuid)
                .flatMapMany(eventFound -> ticketCategoryRepository.findByEventId(eventFound.getId()))
                .map(this::entityToDTO);

    }

    @Override
    public Mono<TicketCategoryDTO> getTicketCategory(UUID uuid) {
        return ticketCategoryRepository.findByUuid(uuid)
                .map(this::entityToDTO);
    }

    @Override
    @Transactional
    public Mono<TicketCategoryDTO> reduceTicketCapacity(UUID ticketUuid, Integer capacity) {
        return ticketCategoryRepository.findByUuid(ticketUuid)
                .flatMap(ticketCategory -> {
                    if (capacity <= ticketCategory.getTicketCapacity()) {
                        ticketCategory.setTicketCapacity(ticketCategory.getTicketCapacity() - capacity);
                        return ticketCategoryRepository.save(ticketCategory);
                    }
                    return Mono.error(new IllegalArgumentException("Capacity exceded"));
                })
                .flatMap(ticketCategory ->
                        eventRepository.findById(ticketCategory.getEventId())
                                .flatMap(eventFound -> {
                                    eventFound.setTotalEventCapacity(eventFound.getTotalEventCapacity() - capacity);
                                    return eventRepository.save(eventFound);
                                })
                                .thenReturn(this.entityToDTO(ticketCategory))
                );
    }

    private TicketCategory dtoToEntity(CreateTicketCategoryDTO createTicketCategoryDTO, Long eventId) {
        return TicketCategory.builder()
                .categoryName(TicketsCategories.valueOf(createTicketCategoryDTO.categoryName()))
                .price(createTicketCategoryDTO.price())
                .ticketCapacity(createTicketCategoryDTO.ticketCapacity())
                .eventId(eventId)
                .build();
    }

    private TicketCategoryDTO entityToDTO(TicketCategory ticketCategory) {
        return new TicketCategoryDTO(
                ticketCategory.getUuid(),
                ticketCategory.getCategoryName().toString(),
                ticketCategory.getTicketCapacity(),
                ticketCategory.getPrice()
        );
    }
}
