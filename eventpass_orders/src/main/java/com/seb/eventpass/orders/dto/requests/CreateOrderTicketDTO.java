package com.seb.eventpass.orders.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateOrderTicketDTO(
        @NotNull(message = "El ticket no tiene una categoria asociada.")
        UUID ticketCategoryUuid,

        @NotNull(message = "El ticket no tiene una cantidad solicitada.")
        @Size(min = 1, message = "El ticket debe solicitar una cantidad mayor a 1.")
        Integer ticketQuantity) {
}
