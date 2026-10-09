package com.seb.eventpass.ticket.services;

import com.seb.eventpass.ticket.dto.request.CreateTicketDTO;
import com.seb.eventpass.ticket.dto.response.TicketDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketService {
    Mono<TicketDTO> createTicket(CreateTicketDTO createTicketDTO);
    Flux<TicketDTO> createTickets(Flux<CreateTicketDTO> createTicketDTO);

    Mono<TicketDTO> getTicketByUuid(UUID uuid);

    Mono<TicketDTO> useTicket(UUID ticketUuid, String ticketCode);

    Flux<TicketDTO> getTicketsByUserUuid(UUID userUuid);
}
