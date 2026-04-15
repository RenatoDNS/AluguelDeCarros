import { Routes } from '@angular/router';

import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/auth-page/auth-page').then((m) => m.AuthPageComponent),
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/dashboard-page/dashboard-page').then((m) => m.DashboardPageComponent),
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./pages/dashboard-home-page/dashboard-home-page').then(
            (m) => m.DashboardHomePageComponent,
          ),
      },
      {
        path: 'descobrir',
        canActivate: [roleGuard],
        data: { userType: 'cliente' },
        loadComponent: () =>
          import('./pages/descobrir-page/descobrir-page').then((m) => m.DescobrirPageComponent),
      },
      {
        path: 'meus-pedidos',
        canActivate: [roleGuard],
        data: { userType: 'cliente' },
        loadComponent: () =>
          import('./pages/meus-pedidos-page/meus-pedidos-page').then(
            (m) => m.MeusPedidosPageComponent,
          ),
      },
      {
        path: 'meus-veiculos',
        canActivate: [roleGuard],
        data: { userType: ['empresa', 'banco'] },
        loadComponent: () =>
          import('./pages/meus-veiculos-page/meus-veiculos-page').then(
            (m) => m.MeusVeiculosPageComponent,
          ),
      },
      {
        path: 'pedidos',
        canActivate: [roleGuard],
        data: { userType: ['empresa', 'banco'] },
        loadComponent: () =>
          import('./pages/pedidos-page/pedidos-page').then((m) => m.PedidosPageComponent),
      },
    ],
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];
