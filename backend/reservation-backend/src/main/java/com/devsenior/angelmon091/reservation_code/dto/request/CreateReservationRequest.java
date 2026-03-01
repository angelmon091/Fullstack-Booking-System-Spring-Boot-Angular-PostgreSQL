package com.devsenior.angelmon091.reservation_code.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request body for creating a new reservation.
 */
public record CreateReservationRequest(
        @NotBlank String customerName,
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotBlank String service
) {}
