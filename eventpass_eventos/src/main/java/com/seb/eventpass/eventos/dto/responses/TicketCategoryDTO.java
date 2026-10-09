package com.seb.eventpass.eventos.dto.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketCategoryDTO(UUID ticketCategoryUuid, String categoryName, Integer ticketCapacity, BigDecimal price) {
}
