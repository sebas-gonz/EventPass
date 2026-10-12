package com.seb.eventpass.payment.services;

import com.seb.eventpass.payment.dto.request.CreatePaymentDTO;
import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
    Mono<PaymentResponseDTO> createPayment(CreatePaymentDTO createPaymentDTO, UUID userUuid);
    Mono<PaymentResponseDTO> findPaymentByUuid(UUID uuid);

    Flux<PaymentResponseDTO> findPaymentByUserUuid(UUID userUuid);
}
