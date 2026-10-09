package com.seb.eventpass.payment.services;

import com.seb.eventpass.payment.dto.request.CreatePaymentDTO;
import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
    Mono<PaymentResponseDTO> createPayment(CreatePaymentDTO createPaymentDTO);
    Mono<PaymentResponseDTO> findPaymentByUuid(UUID uuid);
}
