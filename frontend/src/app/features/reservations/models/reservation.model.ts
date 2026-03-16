/**
 * Reservation status values returned by the API (matches backend enum).
 */
export type ReservationStatus = 'ACTIVE' | 'CANCELLED';

/**
 * Request payload for creating a new reservation.
 */
export interface CreateReservationRequest {
  customerName: string;
  date: string;
  time: string;
  service: string;
}

/**
 * Response payload for a single reservation.
 */
export interface ReservationResponse {
  id: number;
  customerName: string;
  date: string;
  time: string;
  service: string;
  status: ReservationStatus;
}

/** User-facing labels for reservation status. */
export const RESERVATION_STATUS_LABELS: Record<ReservationStatus, string> = {
  ACTIVE: 'Activa',
  CANCELLED: 'Cancelada',
};

/**
 * Services available for reservation (tech store / PC maintenance).
 */
export const AVAILABLE_SERVICES: readonly string[] = [
  'Mantenimiento de PC',
  'Optimización de PC',
  'Diagnóstico de PC',
  'Limpieza de PC (física e interna)',
  'Instalación de software / SO',
  'Compra de RAM',
  'Compra de tarjeta gráfica',
  'Compra de otros componentes',
  'Devolución de RAM',
  'Devolución de tarjeta gráfica',
  'Devolución de otros componentes',
  'Montaje de PC',
  'Asesoría técnica',
];
