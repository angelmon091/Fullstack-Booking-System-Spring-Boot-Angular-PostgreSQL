package com.devsenior.angelmon091.reservation_code.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response payload for a single reservation.
 */
public record ReservationResponse(
        Long id,
        String customerName,
        String customerEmail,
        String customerPhone,
        LocalDate date,
        LocalTime time,
        String service,
        String internalNotes,
        String status
) {}
