import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReservaService } from '../services/reserva.service';
import { AVAILABLE_SERVICES } from '../models/reservation.model';
import type { CreateReservationRequest } from '../models/reservation.model';
import { RouterLink } from '@angular/router';
import { ToastComponent } from '../../../shared/toast/toast.component';

/**
 * Reactive form to create a new reservation.
 */
@Component({
  selector: 'app-create-reservation',
  templateUrl: './create-reservation.component.html',
  styleUrl: './create-reservation.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, RouterLink, ToastComponent],
})
export class CreateReservationComponent {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly reservaService = inject(ReservaService);
  private readonly router = inject(Router);

  readonly availableServices = AVAILABLE_SERVICES;
  readonly submitting = signal(false);
  readonly toastMessage = signal<string | null>(null);

  readonly form = this.fb.group({
    customerName: this.fb.control('', [Validators.required, Validators.minLength(1)]),
    date: this.fb.control('', Validators.required),
    time: this.fb.control('', Validators.required),
    service: this.fb.control('', Validators.required),
  });

  /**
   * Submits the form: creates the reservation via the service and navigates on success.
   */
  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const request: CreateReservationRequest = {
      customerName: value.customerName.trim(),
      date: value.date,
      time: value.time,
      service: value.service,
    };

    this.toastMessage.set(null);
    this.submitting.set(true);
    this.reservaService.create(request).subscribe({
      next: () => {
        this.submitting.set(false);
        this.router.navigate(['/reservas']);
      },
      error: (err) => {
        this.submitting.set(false);
        this.toastMessage.set(
          err?.error?.message ?? err?.message ?? 'Error al guardar la reserva'
        );
      },
    });
  }

  onToastDismiss(): void {
    this.toastMessage.set(null);
  }
}
