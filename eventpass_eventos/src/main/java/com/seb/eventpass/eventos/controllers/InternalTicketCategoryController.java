package com.seb.eventpass.eventos.controllers;

import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.services.ticketcategory.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
@RestController
@RequestMapping("/internal/ticket-categories")
@RequiredArgsConstructor
public class InternalTicketCategoryController {
    private final TicketCategoryService ticketCategoryService;

    @PostMapping("/{ticketuuid}/discount-capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<TicketCategoryDTO> disccountTicketCapacity(@PathVariable UUID ticketuuid, @RequestParam Integer capacity) {
        return ticketCategoryService.reduceTicketCapacity(ticketuuid, capacity);
    }
}
