package com.seb.eventpass.eventos.dto.requests;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTicketCategoryDTO(
        @NotNull(message = "La categoria del ticket debe tener un evento asociado.")
        UUID eventUuid,

        @NotBlank(message = "La categoria del ticket debe tener un nombre.")
        @Size(min = 1, max = 40)
        String categoryName,

        @Min(value = 1, message = "La capacidad de la categoria del ticket debe ser minimo de 1.")
        @NotNull(message = "La categoria del ticket no tiene una capacidad definida.")
        Integer ticketCapacity,

        @DecimalMin(value = "1.0", message = "La categoria del ticket tiene un valor menor a $1 CLP.")
        @NotNull(message = "La categoria del ticket no tiene un valor definido.")
        BigDecimal price) {
}
