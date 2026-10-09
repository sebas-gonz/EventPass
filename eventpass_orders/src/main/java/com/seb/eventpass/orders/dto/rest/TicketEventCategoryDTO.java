package com.seb.eventpass.orders.dto.rest;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketEventCategoryDTO(UUID ticketCategoryUuid,
                                     String categoryName,
                                     BigDecimal price,
                                     Integer capacity) {
}
