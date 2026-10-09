package com.seb.eventpass.orders.dto.responses;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record OrderDTO(UUID orderId,
                       UUID enventId,
                       UUID userUuid,
                       Integer totalTicketsOrdered,
                       BigDecimal totalAmount,
                       Set<TicketOrderedDTO> ticketsCategoryOrdered) {
}
