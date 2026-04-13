import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { finalize } from 'rxjs';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
})
export class LoginPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly form = this.formBuilder.nonNullable.group({
    cpf: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
    password: ['', [Validators.required]],
  });
  readonly cpfInvalid = computed(() => {
    const control = this.form.controls.cpf;
    return control.invalid && (control.touched || control.dirty);
  });
  readonly passwordInvalid = computed(() => {
    const control = this.form.controls.password;
    return control.invalid && (control.touched || control.dirty);
  });

  constructor() {
    if (this.authService.isAuthenticated()) {
      void this.router.navigateByUrl(this.authService.getDashboardRoute());
    }
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    const { cpf, password } = this.form.getRawValue();

    this.authService
      .login({ cpf, password })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          void this.router.navigateByUrl(this.authService.getDashboardRoute());
        },
        error: () => {
          this.errorMessage.set('Falha ao autenticar. Verifique o CPF e a senha informados.');
        },
      });
  }
}
