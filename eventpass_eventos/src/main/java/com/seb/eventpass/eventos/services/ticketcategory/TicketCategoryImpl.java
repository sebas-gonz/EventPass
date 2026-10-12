package com.seb.eventpass.eventos.services.ticketcategory;

import com.seb.eventpass.eventos.dto.requests.CreateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.models.Event;
import com.seb.eventpass.eventos.models.TicketCategory;
import com.seb.eventpass.eventos.models.TicketsCategories;
import com.seb.eventpass.eventos.repositories.EventRepository;
import com.seb.eventpass.eventos.repositories.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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
                    return ticketCategoryRepository.discountTicketCapacity(ticketCategory.getUuid(), capacity)
                            .flatMap(rowsAffected -> {
                                if (rowsAffected <= 0){
                                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Ticker capacity exceded"));
                                }
                                ticketCategory.setTicketCapacity(ticketCategory.getTicketCapacity() - capacity);
                                System.out.println(ticketCategory.getEventId());
                                return eventRepository.discountEventCapacity(ticketCategory.getEventId(), capacity)
                                        .flatMap(eventRowsAffected -> {
                                            if (eventRowsAffected <= 0){
                                                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Event capacity exceded"));
                                            }
                                            ticketCategory.setTicketCapacity(ticketCategory.getTicketCapacity() - capacity);
                                            return Mono.just(ticketCategory);
                                        })
                                        .map(this::entityToDTO);
                            });
                });
    }

    @Override
    public Mono<Void> deleteTicketCategory(UUID uuid) {
        return ticketCategoryRepository.deleteByUuid(uuid);
    }

    @Override
    public Mono<TicketCategoryDTO> updateTicketCategory(UUID uuid, UpdateTicketCategoryDTO updateTicketCategoryDTO) {
        return ticketCategoryRepository.findByUuid(uuid)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Not Ticket Category found with id:" + uuid)))
                .flatMap(ticketFound -> {
                    ticketFound.setCategoryName(TicketsCategories.valueOf(updateTicketCategoryDTO.categoryName()));
                    ticketFound.setPrice(updateTicketCategoryDTO.price());
                    ticketFound.setTicketCapacity(updateTicketCategoryDTO.capacity());
                    return ticketCategoryRepository.save(ticketFound).map(this::entityToDTO);
                });
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
