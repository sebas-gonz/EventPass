package com.seb.eventpass.eventos.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateTicketCategoryDTO(
        @NotBlank(message = "La categoria del ticket debe tener un nombre.")
        @Size(min = 1, max = 40)
        String categoryName,

        @Size(min = 1, message = "La categoria del ticket tiene un valor menor a $1 CLP.")
        @NotNull(message = "La categoria del ticket no tiene un valor definido.")
        BigDecimal price,

        @Size(min = 1, message = "La capacidad de la categoria del ticket debe ser minimo de 1.")
        @NotNull(message = "La categoria del ticket no tiene una capacidad definida.")
        Integer capacity) {
}
