import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { Subject, startWith, switchMap, tap } from 'rxjs';
import { ReservaService } from '../services/reserva.service';
import {
  AVAILABLE_SERVICES,
  RESERVATION_STATUS_LABELS,
  type ReservationResponse,
  type ReservationStatus,
} from '../models/reservation.model';

export type DateFilter = 'all' | 'today' | 'next_week';
export type SortBy = 'date' | 'service';

/**
 * Displays a table of reservations with filters/sort and allows cancelling them.
 */
@Component({
  selector: 'app-list-reservations',
  templateUrl: './list-reservations.component.html',
  styleUrl: './list-reservations.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink],
})
export class ListReservationsComponent {
  private readonly reservaService = inject(ReservaService);

  readonly dateFilter = signal<DateFilter>('all');
  readonly serviceFilter = signal<string>('');
  readonly sortBy = signal<SortBy>('date');
  readonly sortOrder = signal<'asc' | 'desc'>('asc');

  readonly availableServices = AVAILABLE_SERVICES;

  private readonly refresh$ = new Subject<void>();
  readonly loading = signal(true);
  readonly reservations = toSignal(
    this.refresh$.pipe(
      tap(() => this.loading.set(true)),
      startWith(undefined),
      switchMap(() =>
        this.reservaService.getAll().pipe(
          tap(() => this.loading.set(false))
        )
      )
    ),
    { initialValue: [] as ReservationResponse[] }
  );

  readonly filteredReservations = computed(() => {
    const list = this.reservations();
    const dateF = this.dateFilter();
    const serviceF = this.serviceFilter();
    const by = this.sortBy();
    const order = this.sortOrder();

    const today = this.getTodayString();
    const nextWeekEnd = this.getNextWeekEndString();

    let result = list.filter((r) => {
      if (dateF === 'today' && r.date !== today) return false;
      if (dateF === 'next_week') {
        if (r.date < today || r.date > nextWeekEnd) return false;
      }
      if (serviceF && r.service !== serviceF) return false;
      return true;
    });

    result = [...result].sort((a, b) => {
      let cmp: number;
      if (by === 'date') {
        cmp = a.date === b.date
          ? (a.time.localeCompare(b.time))
          : a.date.localeCompare(b.date);
      } else {
        cmp = a.service.localeCompare(b.service, 'es');
        if (cmp === 0) cmp = a.date.localeCompare(b.date) || a.time.localeCompare(b.time);
      }
      return order === 'asc' ? cmp : -cmp;
    });

    return result;
  });

  readonly cancellingId = signal<number | null>(null);
  readonly errorMessage = signal<string | null>(null);

  setDateFilter(value: DateFilter): void {
    this.dateFilter.set(value);
  }

  setServiceFilter(value: string): void {
    this.serviceFilter.set(value);
  }

  /**
   * Handles the service filter select change event.
   */
  onServiceFilterChange(event: Event): void {
    const el = event.target as HTMLSelectElement | null;
    this.serviceFilter.set(el?.value ?? '');
  }

  setSortBy(value: SortBy): void {
    this.sortBy.set(value);
  }

  toggleSortOrder(): void {
    this.sortOrder.update((o) => (o === 'asc' ? 'desc' : 'asc'));
  }

  /**
   * Cancels a reservation by id and refreshes the list on success.
   *
   * @param id the reservation ID to cancel
   */
  cancelReservation(id: number): void {
    this.errorMessage.set(null);
    this.cancellingId.set(id);
    this.reservaService.cancel(id).subscribe({
      next: () => {
        this.cancellingId.set(null);
        this.refresh$.next();
      },
      error: (err) => {
        this.cancellingId.set(null);
        this.errorMessage.set(
          err?.message ?? err?.error?.message ?? 'Error al cancelar la reserva'
        );
      },
    });
  }

  /**
   * Clears the current error message.
   */
  clearError(): void {
    this.errorMessage.set(null);
  }

  /**
   * Returns the user-facing label for a reservation status.
   *
   * @param status the status from the API
   * @returns the label to display (e.g. "Activa", "Cancelada")
   */
  getStatusLabel(status: ReservationStatus): string {
    return RESERVATION_STATUS_LABELS[status] ?? status;
  }

  private getTodayString(): string {
    const d = new Date();
    return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
  }

  private getNextWeekEndString(): string {
    const d = new Date();
    d.setDate(d.getDate() + 6);
    return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
  }
}
