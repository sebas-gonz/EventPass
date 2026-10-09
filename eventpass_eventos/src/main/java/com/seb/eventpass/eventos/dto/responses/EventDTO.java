package com.seb.eventpass.eventos.dto.responses;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record EventDTO(UUID eventUuid,
                       String eventName,
                       String eventDescription,
                       Instant eventDate,
                       String eventAddress,
                       Integer totalEventCapacity,
                       Instant createdAt,
                       Set<TicketCategoryDTO> ticketCategories) {
}
