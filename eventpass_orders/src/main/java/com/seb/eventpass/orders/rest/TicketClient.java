package com.seb.eventpass.orders.rest;

import com.seb.eventpass.orders.dto.rest.TicketDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketClient {
    private final WebClient ticketWebClient;

    public Mono<Void> createTicket(List<TicketDTO> ticketDTOFlux) {
        return ticketWebClient.post()
                .uri("/internal/ticket/saveall")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ticketDTOFlux)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
