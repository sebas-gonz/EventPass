package com.seb.eventpass.orders.exceptions;

import java.time.Instant;

public record ApiErrorResponse(Instant timestamp,
                               Integer status,
                               String error,
                               String message,
                               String path) {
}
