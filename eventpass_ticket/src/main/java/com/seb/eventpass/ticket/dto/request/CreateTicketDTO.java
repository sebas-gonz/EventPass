package com.seb.eventpass.ticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTicketDTO(
        @NotNull(message = "El ticket no tiene una transaccion asociada.")
        UUID paymentUuid,
                              @NotNull(message = "El ticket no tiene un usuario asociado.")
                              UUID userUuid,
                              @NotNull(message = "El ticket no tiene una orden asociada.")
                              UUID orderUuid,
                              @NotNull(message = "El ticket no tiene un evento asociado.")
                              UUID eventUuid,

                              @NotNull(message = "El ticket no tiene una categoria asociada.")
                              UUID ticketEventCategoryUuid,

                              @NotBlank(message = "El ticket no tiene un nombre.")
                              String ticketCategoryName) {
}
