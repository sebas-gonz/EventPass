package com.seb.eventpass.eventos.dto.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateEventDTO(
        @NotBlank(message = "El evento debe tener un nombre.")
        @Size(min = 1, max = 100)
        String eventName,

        @NotBlank(message = "El evento debe tener una descripcion.")
        @Size(min = 1, max = 255)
        String eventDescription,
        @Future(message = "La fecha del evento no puede ser anterior a la fecha actual.")
        @NotNull(message = "El evento debe tener una fecha.")
        Instant eventDate,

        @NotBlank(message = "El evento no tiene una direccion definida.")
        @Size(min = 1, max = 150, message = "La direccion del evento no debe tener entre 1 y 150 carácteres.")
        String eventAddress
) {
}
