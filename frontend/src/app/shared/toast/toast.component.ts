import {
  ChangeDetectionStrategy,
  Component,
  output,
  input,
} from '@angular/core';

/**
 * Toast notification component. Use for error or success messages.
 */
@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ToastComponent {
  /** Message to display. When null or empty, the toast is not shown. */
  message = input<string | null>(null);

  /** Visual style: error (default) or success. */
  type = input<'error' | 'success'>('error');

  /** Emitted when the user dismisses the toast. */
  dismissed = output<void>();

  close(): void {
    this.dismissed.emit();
  }
}
