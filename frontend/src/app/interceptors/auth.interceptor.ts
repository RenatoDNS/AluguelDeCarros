import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';

const PUBLIC_AUTH_PATHS = ['/auth/login', '/clientes', '/bancos', '/empresas'];

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (PUBLIC_AUTH_PATHS.some((path) => req.url.includes(path))) {
    return next(req);
  }

  const authService = inject(AuthService);
  const token = authService.token();

  if (!token) {
    return next(req);
  }

  return next(
    req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    }),
  );
};
