package com.seb.eventpass.orders.rest;

import com.seb.eventpass.orders.dto.rest.TicketEventCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventClient {
    private final WebClient eventWebClient;

    public Mono<TicketEventCategoryDTO> getTicketCategory(UUID eventId, UUID ticketCategoryId) {
        return eventWebClient.get()
                .uri("/api/events/{eventId}/ticket-categories/{ticketCategoryId}", eventId, ticketCategoryId)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.error(new NoSuchElementException("ticketCategory not found")))
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> Mono.error(new NoSuchElementException("bad request")))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, clientResponse -> Mono.error(new NoSuchElementException("server error")))
                .bodyToMono(TicketEventCategoryDTO.class);
    }

    public Mono<TicketEventCategoryDTO> discountTicketCapacity(UUID ticketCategoryId, Integer capacity) {
        return eventWebClient.post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/internal/ticket-categories/{ticketCategoryId}/discount-capacity")
                                .queryParam("capacity", capacity)
                                .build(ticketCategoryId)
                )
                .retrieve()
                .bodyToMono(TicketEventCategoryDTO.class);
    }
}
