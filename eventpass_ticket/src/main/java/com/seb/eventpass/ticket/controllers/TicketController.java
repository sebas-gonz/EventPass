package com.seb.eventpass.ticket.controllers;

import com.seb.eventpass.ticket.dto.request.CreateTicketDTO;
import com.seb.eventpass.ticket.dto.response.TicketDTO;
import com.seb.eventpass.ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService  ticketService;

    @PostMapping("/saveall")
    @ResponseStatus(HttpStatus.CREATED)
     public Flux<TicketDTO> saveAll(@RequestBody Flux<CreateTicketDTO> createTicketDTO) {
        return ticketService.createTickets(createTicketDTO);
    }

    @PostMapping
    public Mono<TicketDTO> createTicket(@RequestBody Mono<CreateTicketDTO> createTicketDTO) {
        return createTicketDTO.flatMap(ticketService::createTicket);
    }

    @GetMapping("/{ticketuuid}")
    public Mono<TicketDTO> getTicketByUuid(@PathVariable("ticketuuid") UUID ticketuuid) {
        return ticketService.getTicketByUuid(ticketuuid);
    }

    @GetMapping("/user")
    public Flux<TicketDTO> getTicketsByUserUuid(@RequestHeader(name = "X-USER-UUID") UUID userUuid) {
        return ticketService.getTicketsByUserUuid(userUuid);
    }

    @GetMapping("/{ticketuuid}/use")
    public Mono<ResponseEntity<TicketDTO>> useTicket(@PathVariable("ticketuuid") UUID ticketuuid, @RequestParam String ticketCode) {
        return ticketService.useTicket(ticketuuid, ticketCode)
                .map(ticketDTO ->
                        ResponseEntity.ok().body(ticketDTO))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
