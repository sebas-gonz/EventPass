package com.seb.eventpass.ticket.controllers;

import com.seb.eventpass.ticket.dto.request.CreateTicketDTO;
import com.seb.eventpass.ticket.dto.response.TicketDTO;
import com.seb.eventpass.ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService  ticketService;

    @GetMapping("/{ticketuuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<TicketDTO> getTicketByUuid(@PathVariable("ticketuuid") UUID ticketuuid) {
        return ticketService.getTicketByUuid(ticketuuid);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Flux<TicketDTO> getTicketsByUserUuid(Principal principal) {
        UUID userUuid = UUID.fromString(principal.getName());
        return ticketService.getTicketsByUserUuid(userUuid);
    }

    @PostMapping("/{ticketuuid}/use")
    @PreAuthorize("hasRole('STAFF')")
    public Mono<ResponseEntity<TicketDTO>> useTicket(@PathVariable("ticketuuid") UUID ticketuuid, @RequestParam String ticketCode) {
        return ticketService.useTicket(ticketuuid, ticketCode)
                .map(ticketDTO ->
                        ResponseEntity.ok().body(ticketDTO))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
