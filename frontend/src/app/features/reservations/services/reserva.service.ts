import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import type {
  CreateReservationRequest,
  ReservationResponse,
} from '../models/reservation.model';

/**
 * Pagination interface matching Spring Data Page.
 */
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

/**
 * Service responsible for reservation API operations.
 * Uses the backend base URL from environment configuration.
 */
@Injectable({
  providedIn: 'root',
})
export class ReservaService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/reservas`;

  /**
   * Fetches a paginated list of reservations from the backend.
   *
   * @param page page number (0-indexed)
   * @param size number of items per page
   * @returns Observable of the Page envelope
   */
  getAll(page = 0, size = 100): Observable<Page<ReservationResponse>> {
    return this.http.get<Page<ReservationResponse>>(`${this.baseUrl}?page=${page}&size=${size}`);
  }

  /**
   * Creates a new reservation.
   *
   * @param request the reservation data
   * @returns Observable of the created reservation
   */
  create(request: CreateReservationRequest): Observable<ReservationResponse> {
    return this.http.post<ReservationResponse>(this.baseUrl, request);
  }

  /**
   * Cancels a reservation by id.
   *
   * @param id the reservation ID
   * @returns Observable of the cancelled reservation
   */
  cancel(id: number): Observable<ReservationResponse> {
    return this.http.delete<ReservationResponse>(`${this.baseUrl}/${id}`);
  }

  /**
   * Purges (permanently deletes) a reservation by id.
   *
   * @param id the reservation ID
   * @returns Observable of the result
   */
  purge(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}/purge`);
  }
}
