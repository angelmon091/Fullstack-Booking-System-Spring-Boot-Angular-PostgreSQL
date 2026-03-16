package com.devsenior.angelmon091.reservation_code.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request body for creating a new reservation.
 */
public record CreateReservationRequest(
        @NotBlank String customerName,
        @Email String customerEmail,
        String customerPhone,
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotBlank String service,
        @Size(max = 1000) String internalNotes
) {}
