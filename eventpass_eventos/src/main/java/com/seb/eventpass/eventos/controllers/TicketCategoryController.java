package com.seb.eventpass.eventos.controllers;

import com.seb.eventpass.eventos.dto.requests.CreateTicketCategoryDTO;
import com.seb.eventpass.eventos.dto.responses.TicketCategoryDTO;
import com.seb.eventpass.eventos.services.ticketcategory.TicketCategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Flux<TicketCategoryDTO> getAllTicketCategories(@PathVariable UUID eventuuid) {
        return ticketCategoryService.getTicketsCategoryByEventUuid(eventuuid);
    }

    @GetMapping("/{ticketuuid}")
    public Mono<TicketCategoryDTO> getTicketCategory(@PathVariable UUID ticketuuid) {
        return ticketCategoryService.getTicketCategory(ticketuuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TicketCategoryDTO> createTicketCategory(@RequestBody CreateTicketCategoryDTO createTicketCategoryDTO) {
        return ticketCategoryService.createTicketCategory(createTicketCategoryDTO);
    }
    @PostMapping("/{ticketuuid}/discount-capacity")
    public Mono<TicketCategoryDTO> disccountTicketCapacity(@PathVariable UUID ticketuuid, @RequestParam Integer capacity) {
        return ticketCategoryService.reduceTicketCapacity(ticketuuid, capacity);
    }
}
