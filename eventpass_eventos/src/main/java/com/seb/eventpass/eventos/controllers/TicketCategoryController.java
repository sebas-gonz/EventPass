package com.seb.eventpass.eventos.controllers;

import com.seb.eventpass.eventos.dto.requests.CreateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.requests.UpdateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.services.ticketcategory.TicketCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventuuid}/ticket-categories")
@RequiredArgsConstructor
public class TicketCategoryController {
    private final TicketCategoryService ticketCategoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<TicketCategoryDTO> getAllTicketCategoriesByEvent(@PathVariable UUID eventuuid) {
        return ticketCategoryService.getTicketsCategoryByEventUuid(eventuuid);
    }

    @GetMapping("/{ticketuuid}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Mono<TicketCategoryDTO> getTicketCategory(@PathVariable UUID ticketuuid) {
        return ticketCategoryService.getTicketCategory(ticketuuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<TicketCategoryDTO> createTicketCategory(@Valid @RequestBody CreateTicketCategoryDTO createTicketCategoryDTO) {
        return ticketCategoryService.createTicketCategory(createTicketCategoryDTO);
    }

    @DeleteMapping("/{ticketUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<Void> deleteTicketCategory(@PathVariable UUID ticketuuid) {
        return  ticketCategoryService.deleteTicketCategory(ticketuuid);
    }

    @PutMapping("/{ticketuuid}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<TicketCategoryDTO> updateTicketCategory(@PathVariable UUID ticketuuid, @RequestBody Mono<UpdateTicketCategoryDTO>  updateTicketCategoryDTO) {
        return updateTicketCategoryDTO.flatMap(updateTicketCategory -> ticketCategoryService.updateTicketCategory(ticketuuid, updateTicketCategory));
    }
}
