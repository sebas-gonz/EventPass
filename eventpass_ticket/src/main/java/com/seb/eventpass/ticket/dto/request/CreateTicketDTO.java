package com.seb.eventpass.ticket.dto.request;

import java.util.UUID;

public record CreateTicketDTO(UUID paymentUuid,
                              UUID userUuid,
                              UUID orderUuid,
                              UUID eventUuid,
                              UUID ticketEventCategoryUuid,
                              String ticketCategoryName) {
}
