package com.seb.eventpass.orders.services.ticketorder;

import com.seb.eventpass.orders.dto.requests.CreateOrderTicketDTO;
import com.seb.eventpass.orders.dto.responses.TicketOrderedDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketOrderService {
    Mono<TicketOrderedDTO> createTicketCategory(CreateOrderTicketDTO createOrderTicketDTO, UUID eventId, Long orderId);
    Mono<TicketOrderedDTO> getTicketCategory(UUID uuid);
    Flux<TicketOrderedDTO> getAllTicketCategories();
    Flux<TicketOrderedDTO> getTicketCategoriesByOrderUuid(UUID orderUuid);
}
