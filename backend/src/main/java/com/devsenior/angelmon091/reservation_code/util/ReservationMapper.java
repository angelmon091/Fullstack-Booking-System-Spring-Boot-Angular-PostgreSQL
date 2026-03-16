package com.devsenior.angelmon091.reservation_code.util;

import com.devsenior.angelmon091.reservation_code.dto.request.CreateReservationRequest;
import com.devsenior.angelmon091.reservation_code.dto.response.ReservationResponse;
import com.devsenior.angelmon091.reservation_code.entity.Reservation;
import org.springframework.stereotype.Component;

/**
 * Maps reservation DTOs to entities and vice versa.
 */
@Component
public class ReservationMapper {

    /**
     * Maps a create reservation request DTO to a reservation entity.
     * Does not set id nor status; the service sets status according to business rules.
     *
     * @param request the creation request (must not be null)
     * @return the reservation entity ready to be completed and persisted
     */
    public Reservation toEntity(CreateReservationRequest request) {
        return Reservation.builder()
                .customerName(request.customerName())
                .customerEmail(request.customerEmail())
                .customerPhone(request.customerPhone())
                .date(request.date())
                .time(request.time())
                .service(request.service())
                .internalNotes(request.internalNotes())
                .build();
    }

    /**
     * Maps a reservation entity to its response DTO.
     *
     * @param reservation the entity (must not be null)
     * @return the response DTO
     */
    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getCustomerEmail(),
                reservation.getCustomerPhone(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getService(),
                reservation.getInternalNotes(),
                reservation.getStatus().name()
        );
    }
}
