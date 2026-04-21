import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

import { catchError, throwError } from 'rxjs';

const TOKEN_STORAGE_KEY = 'auth_token';
const USER_TYPE_STORAGE_KEY = 'auth_user_type';

export const authErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse && error.status === 401) {
        sessionStorage.removeItem(TOKEN_STORAGE_KEY);
        sessionStorage.removeItem(USER_TYPE_STORAGE_KEY);
        router.navigate(['/login']);
      }

      return throwError(() => error);
    }),
  );
};
