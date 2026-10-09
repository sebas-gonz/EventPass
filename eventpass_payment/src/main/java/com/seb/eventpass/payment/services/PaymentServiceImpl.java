package com.seb.eventpass.payment.services;

import com.seb.eventpass.payment.dto.request.CreatePaymentDTO;
import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import com.seb.eventpass.payment.models.Payment;
import com.seb.eventpass.payment.models.PaymentStatus;
import com.seb.eventpass.payment.repositories.PaymentRepository;
import com.seb.eventpass.payment.rest.OrderWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final OrderWebClient orderWebClient;

    @Override
    public Mono<PaymentResponseDTO> createPayment(CreatePaymentDTO createPaymentDTO) {
        Payment newPayment = dtoToEntity(createPaymentDTO);
        boolean isApproved = (newPayment.getAmount().compareTo(BigDecimal.valueOf(1000)) != 0);
        newPayment.setPaymentStatus(isApproved ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
        return paymentRepository.save(newPayment)
                .flatMap(savedPayment -> {
                    Mono<Void> notifyOrder;
                    if (savedPayment.getPaymentStatus().equals(PaymentStatus.APPROVED)) {
                        notifyOrder = orderWebClient.approveOrder(savedPayment.getOrderUuid(), savedPayment.getUuid());
                    } else  {
                        notifyOrder = orderWebClient.rejectOrder(savedPayment.getOrderUuid());
                    }
                    return notifyOrder.thenReturn(savedPayment);
                })
                .map(this::entityToDto);
    }

    @Override
    public Mono<PaymentResponseDTO> findPaymentByUuid(UUID uuid) {
        return paymentRepository.findByUuid(uuid)
                .map(this::entityToDto);
    }

    private Payment dtoToEntity(CreatePaymentDTO createPaymentDTO) {
        return Payment.builder()
                .cvv(createPaymentDTO.cvv())
                .amount(createPaymentDTO.amount())
                .cardHolder(createPaymentDTO.cardHolder())
                .cardNumber(createPaymentDTO.cardHolder())
                .userUuid(createPaymentDTO.userUuid())
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
