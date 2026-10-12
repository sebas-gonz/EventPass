package com.seb.eventpass.payment.services;

import com.seb.eventpass.payment.dto.request.CreatePaymentDTO;
import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import com.seb.eventpass.payment.models.Payment;
import com.seb.eventpass.payment.models.PaymentStatus;
import com.seb.eventpass.payment.repositories.PaymentRepository;
import com.seb.eventpass.payment.rest.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Override
    public Mono<PaymentResponseDTO> createPayment(CreatePaymentDTO createPaymentDTO, UUID userUuid) {
        Payment newPayment = dtoToEntity(createPaymentDTO);
        newPayment.setUserUuid(userUuid);

        boolean isApproved = (newPayment.getAmount().compareTo(BigDecimal.valueOf(1000)) >= 0);
        newPayment.setPaymentStatus(isApproved ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);

        return paymentRepository.save(newPayment)
                .flatMap(savedPayment -> {
                    Mono<Void> notifyOrder;
                    if (savedPayment.getPaymentStatus().equals(PaymentStatus.APPROVED)) {
                        notifyOrder = orderClient.approveOrder(savedPayment.getOrderUuid(), savedPayment.getUuid());
                    } else {
                        notifyOrder = orderClient.rejectOrder(savedPayment.getOrderUuid());
                    }
                    return notifyOrder.thenReturn(savedPayment)
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                savedPayment.setPaymentStatus(PaymentStatus.REJECTED);

                                return paymentRepository.save(savedPayment)
                                        .flatMap(_ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Event Capacity exceeded, returning payment")));

                            });
                })
                .map(this::entityToDto);
    }

    @Override
    public Mono<PaymentResponseDTO> findPaymentByUuid(UUID uuid) {
        return paymentRepository.findByUuid(uuid)
                .map(this::entityToDto);
    }

    @Override
    public Flux<PaymentResponseDTO> findPaymentByUserUuid(UUID userUuid) {
        return paymentRepository.findByUserUuid(userUuid)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No payments with the userUuid: " + userUuid)))
                .map(this::entityToDto);
    }

    private Payment dtoToEntity(CreatePaymentDTO createPaymentDTO) {
        return Payment.builder()
                .cvv(createPaymentDTO.cvv())
                .amount(createPaymentDTO.amount())
                .cardHolder(createPaymentDTO.cardHolder())
                .cardNumber(createPaymentDTO.cardNumber())
                .orderUuid(createPaymentDTO.orderUuid())
                .build();
    }

    private PaymentResponseDTO entityToDto(Payment payment) {
        return new PaymentResponseDTO(
                payment.getOrderUuid(),
                payment.getUserUuid(),
                payment.getUuid(),
                payment.getPaymentStatus().toString(),
                payment.getCardHolder(),
                payment.getAmount()
        );
    }
}
