package com.seb.eventpass.orders.dto.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketOrderedDTO(UUID ticketOrderId,
                               UUID ticketCategoryId,
                               String ticketCategoryName,
                               Integer ticketQuantity,
                               BigDecimal ticketPrice) {
}
