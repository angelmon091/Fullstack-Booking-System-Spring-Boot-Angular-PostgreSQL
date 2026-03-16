package com.devsenior.angelmon091.reservation_code.repository;

import com.devsenior.angelmon091.reservation_code.entity.Reservation;
import com.devsenior.angelmon091.reservation_code.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Spring Data repository for {@link Reservation} persistence operations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Checks whether a reservation already exists for the given date and time.
     *
     * @param date the reservation date (must not be null)
     * @param time the reservation time (must not be null)
     * @return true if there is at least one reservation for the given date and time; false otherwise
     */
    boolean existsByDateAndTime(LocalDate date, LocalTime time);

    /**
     * Checks whether a reservation with the given status exists for the given date and time.
     *
     * @param date   the reservation date (must not be null)
     * @param time   the reservation time (must not be null)
     * @param status the reservation status to filter by (must not be null)
     * @return true if there is at least one matching reservation; false otherwise
     */
    boolean existsByDateAndTimeAndStatus(LocalDate date, LocalTime time, ReservationStatus status);
}

