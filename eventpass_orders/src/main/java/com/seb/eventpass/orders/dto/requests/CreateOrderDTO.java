package com.seb.eventpass.orders.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record CreateOrderDTO(
        @NotNull(message = "La orden no tiene un evento asociado.")
        UUID eventUuid,

        @NotNull(message = "La orden no tiene tickets asociados.")
        @Size(min = 1, message = "La orden debe tener al menos un ticket asociado.")
        Set<CreateOrderTicketDTO> ticketsOrdered) {
}
