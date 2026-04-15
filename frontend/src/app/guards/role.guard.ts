import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { type UserType } from '../models/auth';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedUserType = route.data?.['userType'] as UserType | UserType[] | undefined;

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if (!expectedUserType) {
    return true;
  }

  const expectedUserTypes = Array.isArray(expectedUserType) ? expectedUserType : [expectedUserType];

  if (expectedUserTypes.some((userType) => authService.hasUserType(userType))) {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};
