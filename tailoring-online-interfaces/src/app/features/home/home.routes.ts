import { Routes } from '@angular/router';

export const HOME_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./home.component').then(m => m.HomeComponent),
    children: [
      {
        path: '',
        loadComponent: () => import('./_layout/home-layout.component').then(m => m.HomeLayoutComponent),
      },
      {
        path: 'auth',
        loadChildren: () => import('./_components/_auth/auth.routes').then(m => m.AUTH_ROUTES),
      }
    ]
  }
];
