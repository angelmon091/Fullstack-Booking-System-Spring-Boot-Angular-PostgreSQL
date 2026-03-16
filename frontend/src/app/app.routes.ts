import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'reservas',
    loadComponent: () =>
      import(
        './features/reservations/list-reservations/list-reservations.component'
      ).then((m) => m.ListReservationsComponent),
  },
  {
    path: 'reservas/nueva',
    loadComponent: () =>
      import(
        './features/reservations/create-reservation/create-reservation.component'
      ).then((m) => m.CreateReservationComponent),
  },
  { path: '', redirectTo: 'reservas', pathMatch: 'full' },
];
