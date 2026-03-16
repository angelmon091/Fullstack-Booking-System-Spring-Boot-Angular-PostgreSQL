package com.devsenior.angelmon091.reservation_code.service;

import com.devsenior.angelmon091.reservation_code.dto.request.CreateReservationRequest;
import com.devsenior.angelmon091.reservation_code.dto.response.ReservationResponse;
import com.devsenior.angelmon091.reservation_code.entity.Reservation;
import com.devsenior.angelmon091.reservation_code.entity.ReservationStatus;
import com.devsenior.angelmon091.reservation_code.exception.BusinessRuleViolationException;
import com.devsenior.angelmon091.reservation_code.repository.ReservationRepository;
import com.devsenior.angelmon091.reservation_code.util.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Business logic for reservation management.
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    /**
     * Retrieves a paginated list of all reservations.
     *
     * @param pageable pagination and sorting information
     * @return a page of reservation response DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReservationResponse> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(reservationMapper::toResponse);
    }

    /**
     * Creates a new reservation only if no active reservation exists for the same date and time.
     *
     * @param request the creation data (must not be null, must be valid)
     * @return the created reservation as a response DTO with ACTIVE status
     * @throws BusinessRuleViolationException if an active reservation already exists for the given date and time
     */
    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeAndStatus(
                request.date(), request.time(), ReservationStatus.ACTIVE)) {
            throw new BusinessRuleViolationException(
                    "A reservation already exists for date %s at time %s".formatted(request.date(), request.time()));
        }
        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toResponse(reservation);
    }

    /**
     * Cancels a reservation by its identifier.
     *
     * @param id the reservation ID (must not be null)
     * @return the cancelled reservation as a response DTO
     * @throws BusinessRuleViolationException if the reservation does not exist or is already cancelled
     */
    @Transactional
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleViolationException("Reservation with id %d not found".formatted(id)));
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessRuleViolationException("Reservation with id %d is already cancelled".formatted(id));
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toResponse(reservation);
    }

    /**
     * Physically deletes a reservation from the system.
     * This is intended for cleaning up junk or test data.
     *
     * @param id the reservation ID
     */
    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new BusinessRuleViolationException("Reservation with id %d not found".formatted(id));
        }
        reservationRepository.deleteById(id);
    }
}
