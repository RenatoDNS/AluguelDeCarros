import { HttpInterceptorFn } from '@angular/common/http';

const TOKEN_STORAGE_KEY = 'auth_token';
const PUBLIC_AUTH_PATHS = ['/auth/login', '/clientes', '/bancos', '/empresas'];

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (PUBLIC_AUTH_PATHS.some((path) => req.url.includes(path))) {
    return next(req);
  }

  const token = sessionStorage.getItem(TOKEN_STORAGE_KEY);

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
