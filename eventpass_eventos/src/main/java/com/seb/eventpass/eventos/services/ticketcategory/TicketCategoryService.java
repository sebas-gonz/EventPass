package com.seb.eventpass.eventos.services.ticketcategory;

import com.seb.eventpass.eventos.dto.requests.CreateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketCategoryService {
    Mono<TicketCategoryDTO> createTicketCategory(CreateTicketCategoryDTO createTicketCategoryDTO);
    Flux<TicketCategoryDTO> getTicketsCategoryByEventUuid(UUID eventUuid);
    Mono<TicketCategoryDTO> getTicketCategory(UUID uuid);
    Mono<TicketCategoryDTO> reduceTicketCapacity(UUID ticketUuid, Integer capacity);
    Mono<Void> deleteTicketCategory(UUID uuid);
    Mono<TicketCategoryDTO> updateTicketCategory(UUID uuid, UpdateTicketCategoryDTO updateTicketCategoryDTO);
}
