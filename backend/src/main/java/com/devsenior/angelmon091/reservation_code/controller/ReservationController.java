package com.devsenior.angelmon091.reservation_code.controller;

import com.devsenior.angelmon091.reservation_code.dto.request.CreateReservationRequest;
import com.devsenior.angelmon091.reservation_code.dto.response.ReservationResponse;
import com.devsenior.angelmon091.reservation_code.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * REST controller for reservation management.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Lists all reservations.
     *
     * @return list of all reservations (200 OK)
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> listAll() {
        List<ReservationResponse> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Creates a new reservation.
     *
     * @param request the reservation data (must be valid)
     * @return the created reservation (201 Created) with Location header
     */
    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse created = reservationService.createReservation(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(created);
    }

    /**
     * Cancels a reservation by id.
     *
     * @param id the reservation ID
     * @return the cancelled reservation (200 OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        ReservationResponse cancelled = reservationService.cancelReservation(id);
        return ResponseEntity.ok(cancelled);
    }
}
