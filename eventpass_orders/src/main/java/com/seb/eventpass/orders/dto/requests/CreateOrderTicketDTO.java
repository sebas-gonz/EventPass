package com.seb.eventpass.orders.dto.requests;

import java.util.UUID;

public record CreateOrderTicketDTO(UUID ticketCategoryUuid, Integer ticketQuantity) {
}
