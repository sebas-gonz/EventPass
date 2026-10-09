package com.seb.eventpass.eventos.dto.requests;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTicketCategoryDTO(UUID eventUuid, String categoryName,Integer ticketCapacity, BigDecimal price) {
}
