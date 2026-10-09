package com.seb.eventpass.payment.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentDTO(UUID orderUuid,
                               UUID userUuid,
                               String cardNumber,
                               String cardHolder,
                               String cvv,
                               BigDecimal amount) {
}
