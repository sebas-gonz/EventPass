package com.seb.eventpass.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentDTO(
        @NotNull(message = "La transaccion no tiene una orden asociada.")
        UUID orderUuid,

        @NotBlank(message = "La transaccion no tiene un numero de tarjeta asociado.")
        @Size(min = 13, max = 19, message = "Inserte un numero de tarjeta valido.")
        String cardNumber,

        @NotBlank(message = "La tarje debe tener un propietario.")
        String cardHolder,

        @NotBlank(message = "La transaccion necesito del codigo de seguridad.")
        @Size(min = 1, max = 3, message = "Ingrese un codigo cvv valido.")
        String cvv,

        @NotNull(message = "El monto de la transaccion no esta definido.")
        BigDecimal amount) {
}
