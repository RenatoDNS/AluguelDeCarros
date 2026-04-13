import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService, UserType } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedUserType = route.data?.['userType'] as UserType | undefined;

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if (!expectedUserType || authService.hasUserType(expectedUserType)) {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};
