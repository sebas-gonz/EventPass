package com.seb.eventpass.orders.rest;

import com.seb.eventpass.orders.dto.rest.TicketDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TicketWebClient {
    private final WebClient ticketWebClient;

    public Mono<Void> createTicket(Flux<TicketDTO> ticketDTOFlux) {
        return ticketWebClient.post()
                .uri("/api/")
                .accept(MediaType.APPLICATION_JSON)
                .body(ticketDTOFlux, TicketDTO.class)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
