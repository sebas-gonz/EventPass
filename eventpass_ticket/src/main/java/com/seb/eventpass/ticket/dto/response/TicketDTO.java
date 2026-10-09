package com.seb.eventpass.ticket.dto.response;

import java.util.UUID;

public record TicketDTO(UUID ticketUuid,
                        UUID userUuid,
                        UUID orderUuid,
                        UUID eventUuid,
                        String ticketCode,
                        UUID ticketEventCategoryUuid,
                        boolean isUsed,
                        String ticketCategoryName) {
}
