package com.seb.eventpass.ticket.controllers;

import com.seb.eventpass.ticket.dto.request.CreateTicketDTO;
import com.seb.eventpass.ticket.dto.response.TicketDTO;
import com.seb.eventpass.ticket.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/internal/ticket")
@RequiredArgsConstructor
public class InternalTicketController {
    private final TicketService ticketService;

    @PostMapping("/saveall")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Flux<TicketDTO> saveAll(@Valid @RequestBody List<CreateTicketDTO> createTicketDTO) {
        return ticketService.createTickets(createTicketDTO);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<TicketDTO> createTicket(@Valid @RequestBody Mono<CreateTicketDTO> createTicketDTO) {
        return createTicketDTO.flatMap(ticketService::createTicket);
    }
}
