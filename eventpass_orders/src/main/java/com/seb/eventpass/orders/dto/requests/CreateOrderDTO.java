package com.seb.eventpass.orders.dto.requests;

import java.util.Set;
import java.util.UUID;

public record CreateOrderDTO(UUID eventUuid, Set<CreateOrderTicketDTO> ticketsOrdered) {
}
