import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  type ValidationErrors,
  type ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';

import { NgxMaskDirective } from 'ngx-mask';
import { finalize } from 'rxjs';

import {
  type BancoRegisterPayload,
  type ClienteRegisterPayload,
  type EmpresaRegisterPayload,
  type EntidadeEmpregadoraPayload,
} from '../../models/auth';
import { AuthService } from '../../services/auth.service';

type AuthMode = 'login' | 'register';
type RegisterUserType = 'cliente' | 'empresa' | 'banco';

const MAX_ENTIDADES_EMPREGADORAS = 3;
const CNPJ_MASK_PATTERNS = {
  X: { pattern: new RegExp('[A-Za-z0-9]') },
};

function digitsOnly(value: unknown) {
  return String(value ?? '').replace(/\D/g, '');
}

function alphaNumericOnly(value: unknown) {
  return String(value ?? '').replace(/[^A-Za-z0-9]/g, '');
}

function exactDigitsValidator(length: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = digitsOnly(control.value);

    if (!value) {
      return null;
    }

    return value.length === length ? null : { exactDigits: true };
  };
}

function cpfOrCnpjDigitsValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = digitsOnly(control.value);

    if (!value) {
      return null;
    }

    return value.length === 11 || value.length === 14 ? null : { cpfOrCnpjDigits: true };
  };
}

function maxDigitsValidator(length: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const rawValue = String(control.value ?? '').trim();

    if (!rawValue) {
      return null;
    }

    return /^\d+$/.test(rawValue) && rawValue.length <= length ? null : { maxDigits: true };
  };
}

function exactAlphaNumericValidator(length: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = alphaNumericOnly(control.value);

    if (!value) {
      return null;
    }

    return value.length === length ? null : { exactAlphaNumeric: true };
  };
}

function currencyValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = parseCurrencyValue(control.value);

    if (String(control.value ?? '').trim() === '') {
      return null;
    }

    return value === null ? { currency: true } : null;
  };
}

function parseCurrencyValue(value: unknown) {
  const normalizedValue = String(value ?? '')
    .replace(/\s/g, '')
    .replace('R$', '')
    .replace(/\./g, '')
    .replace(',', '.')
    .trim();

  if (!normalizedValue) {
    return null;
  }

  const parsedValue = Number(normalizedValue);
  return Number.isFinite(parsedValue) ? parsedValue : null;
}

@Component({
  selector: 'app-auth-page',
  imports: [ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './auth-page.html',
  styleUrl: './auth-page.css',
})
export class AuthPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly mode = signal<AuthMode>('login');
  readonly registerUserType = signal<RegisterUserType>('cliente');
  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly successMessage = signal('');
  readonly entidadeEmpregadoraDialogOpen = signal(false);
  readonly entidadesEmpregadoras = signal<EntidadeEmpregadoraPayload[]>([]);
  readonly canAddEntidadeEmpregadora = computed(
    () => this.entidadesEmpregadoras().length < MAX_ENTIDADES_EMPREGADORAS,
  );
  readonly cnpjMaskPatterns = CNPJ_MASK_PATTERNS;
  readonly maxEntidadesEmpregadoras = MAX_ENTIDADES_EMPREGADORAS;

  readonly isLoginMode = computed(() => this.mode() === 'login');

  readonly loginForm = this.formBuilder.nonNullable.group({
    login: ['', [Validators.required, cpfOrCnpjDigitsValidator()]],
    password: ['', [Validators.required]],
  });

  readonly clienteRegisterForm = this.formBuilder.nonNullable.group({
    rg: ['', [Validators.required, maxDigitsValidator(11)]],
    cpf: ['', [Validators.required, exactDigitsValidator(11)]],
    nome: ['', [Validators.required]],
    endereco: ['', [Validators.required]],
    profissao: ['', [Validators.required]],
    senha: ['', [Validators.required]],
  });

  readonly empresaRegisterForm = this.formBuilder.nonNullable.group({
    razaoSocial: ['', [Validators.required]],
    cnpj: ['', [Validators.required, exactAlphaNumericValidator(14)]],
    ramoDeAtividade: ['', [Validators.required]],
    senha: ['', [Validators.required]],
  });

  readonly bancoRegisterForm = this.formBuilder.nonNullable.group({
    razaoSocial: ['', [Validators.required]],
    cnpj: ['', [Validators.required, exactAlphaNumericValidator(14)]],
    codigoBancario: ['', [Validators.required]],
    senha: ['', [Validators.required]],
  });

  readonly entidadeEmpregadoraForm = this.formBuilder.nonNullable.group({
    nomeEmpresa: ['', [Validators.required]],
    cnpj: ['', [Validators.required, exactAlphaNumericValidator(14)]],
    rendimento: ['', [Validators.required, currencyValidator()]],
  });

  constructor() {
    if (this.authService.isAuthenticated()) {
      void this.router.navigateByUrl('/dashboard');
    }
  }

  setMode(mode: AuthMode) {
    this.mode.set(mode);
    this.clearMessages();
  }

  setRegisterUserType(userType: RegisterUserType) {
    this.registerUserType.set(userType);
    this.clearMessages();
  }

  isInvalid(control: AbstractControl | null) {
    return Boolean(control && control.invalid && (control.touched || control.dirty));
  }

  openEntidadeEmpregadoraDialog() {
    if (!this.canAddEntidadeEmpregadora()) {
      return;
    }

    this.entidadeEmpregadoraDialogOpen.set(true);
    this.entidadeEmpregadoraForm.markAsPristine();
    this.entidadeEmpregadoraForm.markAsUntouched();
  }

  closeEntidadeEmpregadoraDialog() {
    this.entidadeEmpregadoraDialogOpen.set(false);
    this.entidadeEmpregadoraForm.reset({
      nomeEmpresa: '',
      cnpj: '',
      rendimento: '',
    });
  }

  addEntidadeEmpregadora() {
    if (!this.canAddEntidadeEmpregadora()) {
      return;
    }

    if (this.entidadeEmpregadoraForm.invalid) {
      this.entidadeEmpregadoraForm.markAllAsTouched();
      return;
    }

    const { nomeEmpresa, cnpj, rendimento } = this.entidadeEmpregadoraForm.getRawValue();
    const parsedRendimento = parseCurrencyValue(rendimento);

    if (parsedRendimento === null) {
      this.entidadeEmpregadoraForm.controls.rendimento.markAsTouched();
      return;
    }

    this.entidadesEmpregadoras.update((current) => [
      ...current,
      {
        nomeEmpresa,
        cnpj: alphaNumericOnly(cnpj),
        rendimento: parsedRendimento,
      },
    ]);

    this.closeEntidadeEmpregadoraDialog();
  }

  removeEntidadeEmpregadora(index: number) {
    this.entidadesEmpregadoras.update((current) =>
      current.filter((_, currentIndex) => currentIndex !== index),
    );
  }

  submitLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.clearMessages();

    const { login, password } = this.loginForm.getRawValue();

    this.authService
      .login({ login: digitsOnly(login), password })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          void this.router.navigateByUrl('/dashboard');
        },
        error: () => {
          this.errorMessage.set('Falha ao autenticar. Verifique o CPF e a senha informados.');
        },
      });
  }

  submitRegister() {
    const payload = this.buildRegisterPayload();

    if (!payload) {
      return;
    }

    this.loading.set(true);
    this.clearMessages();

    this.authService
      .register(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.resetCurrentRegisterForm();
          this.successMessage.set('Cadastro realizado com sucesso.');
        },
        error: () => {
          this.errorMessage.set('Falha ao cadastrar. Revise os dados informados.');
        },
      });
  }

  private buildRegisterPayload():
    | ClienteRegisterPayload
    | EmpresaRegisterPayload
    | BancoRegisterPayload
    | null {
    switch (this.registerUserType()) {
      case 'cliente': {
        if (this.clienteRegisterForm.invalid) {
          this.clienteRegisterForm.markAllAsTouched();
          return null;
        }

        const { rg, cpf, nome, endereco, profissao, senha } =
          this.clienteRegisterForm.getRawValue();

        return {
          rg: digitsOnly(rg),
          cpf: digitsOnly(cpf),
          nome,
          endereco,
          profissao,
          senha,
          entidadesEmpregadoras: this.entidadesEmpregadoras(),
        };
      }
      case 'empresa': {
        if (this.empresaRegisterForm.invalid) {
          this.empresaRegisterForm.markAllAsTouched();
          return null;
        }

        const { razaoSocial, cnpj, ramoDeAtividade, senha } =
          this.empresaRegisterForm.getRawValue();

        return {
          razaoSocial,
          cnpj: alphaNumericOnly(cnpj),
          ramoDeAtividade,
          senha,
        };
      }
      case 'banco': {
        if (this.bancoRegisterForm.invalid) {
          this.bancoRegisterForm.markAllAsTouched();
          return null;
        }

        const { razaoSocial, cnpj, codigoBancario, senha } = this.bancoRegisterForm.getRawValue();

        return {
          razaoSocial,
          cnpj: alphaNumericOnly(cnpj),
          codigoBancario,
          senha,
        };
      }
    }
  }

  private resetCurrentRegisterForm() {
    switch (this.registerUserType()) {
      case 'cliente':
        this.clienteRegisterForm.reset({
          rg: '',
          cpf: '',
          nome: '',
          endereco: '',
          profissao: '',
          senha: '',
        });
        this.entidadesEmpregadoras.set([]);
        break;
      case 'empresa':
        this.empresaRegisterForm.reset({
          razaoSocial: '',
          cnpj: '',
          ramoDeAtividade: '',
          senha: '',
        });
        break;
      case 'banco':
        this.bancoRegisterForm.reset({
          razaoSocial: '',
          cnpj: '',
          codigoBancario: '',
          senha: '',
        });
        break;
    }
  }

  private clearMessages() {
    this.errorMessage.set('');
    this.successMessage.set('');
  }
}
