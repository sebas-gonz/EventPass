package com.seb.eventpass.payment.repositories;

import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import com.seb.eventpass.payment.models.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {
    Mono<Payment> findByUuid(UUID uuid);
}
