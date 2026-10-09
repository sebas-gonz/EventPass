package com.seb.eventpass.payment.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderWebClient {
    private final WebClient orderWebClient;

    public Mono<Void> approveOrder(UUID orderUuid, UUID paymentUuid) {
        return orderWebClient.post()
                .uri("/orders/{orderUuid}/approved", orderUuid)
                .header("X-PAYMENT-UUID", paymentUuid.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> rejectOrder(UUID orderUuid) {
        return orderWebClient.post()
                .uri("/api/orders/{orderUuid}/rejected", orderUuid)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
