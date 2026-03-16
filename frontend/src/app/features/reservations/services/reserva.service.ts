import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import type {
  CreateReservationRequest,
  ReservationResponse,
} from '../models/reservation.model';

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
   * Fetches all reservations from the backend.
   *
   * @returns Observable of the list of reservations
   */
  getAll(): Observable<ReservationResponse[]> {
    return this.http.get<ReservationResponse[]>(this.baseUrl);
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
}
