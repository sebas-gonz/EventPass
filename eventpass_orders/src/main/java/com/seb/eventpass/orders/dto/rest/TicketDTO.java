package com.seb.eventpass.orders.dto.rest;

import java.util.UUID;

public record TicketDTO(UUID paymentUuid,
                        UUID userUuid,
                        UUID orderUuid,
                        UUID eventUuid,
                        UUID ticketEventCategoryUuid,
                        String ticketCategoryName) {
}
