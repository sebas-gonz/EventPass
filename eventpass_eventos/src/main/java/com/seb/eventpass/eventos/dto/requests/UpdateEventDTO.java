package com.seb.eventpass.eventos.dto.requests;

import java.time.Instant;

public record UpdateEventDTO(String eventName,
                             String eventDescription,
                             Instant eventDate,
                             String eventAddress) {
}
