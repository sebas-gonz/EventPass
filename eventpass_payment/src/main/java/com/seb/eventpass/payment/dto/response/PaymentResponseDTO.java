package com.seb.eventpass.payment.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponseDTO(UUID orderUuid,
                                 UUID userUuid,
                                 UUID paymentUuid,
                                 String paymentStatus,
                                 String cardHolder,
                                 BigDecimal amount) {
}
